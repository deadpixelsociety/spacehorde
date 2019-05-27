package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool

abstract class Script : Pool.Poolable {
    var started = false
    var finished = false

    open fun start(engine: Engine, entity: Entity) {
    }

    open fun finish(engine: Engine, entity: Entity) {
    }

    abstract fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean

    override fun reset() {
        started = false
        finished = false
    }
}