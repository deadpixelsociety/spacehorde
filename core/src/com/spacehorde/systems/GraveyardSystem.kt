package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.spacehorde.components.Dead

class GraveyardSystem : IteratingSystem(Family.all(Dead::class.java).get()) {
    private val entitiesToRemove = mutableListOf<Entity>()

    override fun update(deltaTime: Float) {
        entitiesToRemove.clear()
        super.update(deltaTime)
        entitiesToRemove.forEach { engine.removeEntity(it) }
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return
        println("$entity is dead")
        entitiesToRemove.add(entity)
    }
}