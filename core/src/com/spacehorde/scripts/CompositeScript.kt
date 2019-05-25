package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.gdxArray

open class CompositeScript : Script() {
    val scripts = gdxArray<Script>()

    override fun start(engine: Engine, entity: Entity) {
        scripts.forEach {
            if (!it.started) {
                it.start(engine, entity)
                it.started = true
            }
        }
    }

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        scripts.toList().forEach {
            if (it.update(deltaTime, engine, entity)) {
                scripts.removeValue(it, true)
                it.finish(engine, entity)
            }
        }

        return scripts.size == 0
    }

    override fun finish(engine: Engine, entity: Entity) {
        scripts.forEach { it.finish(engine, entity) }
        scripts.clear()
    }

    override fun reset() {
        scripts.clear()
    }
}