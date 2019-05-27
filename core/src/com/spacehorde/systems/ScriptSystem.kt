package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.spacehorde.components.Scripted
import com.spacehorde.components.mapper

class ScriptSystem : IteratingSystem(Family.all(Scripted::class.java).get()) {
    private val scriptedMapper by mapper<Scripted>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return
        val scripted = scriptedMapper[entity] ?: return
        scripted.scripts.toList().forEach {
            if (!it.started) {
                it.start(engine, entity)
                it.started = true
            }

            if (it.finished || it.update(deltaTime, engine, entity)) {
                scripted.scripts.removeValue(it, true)
                it.finish(engine, entity)
            }
        }
    }
}