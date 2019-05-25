package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

abstract class DurationScript : Script() {
    var duration = Duration.INFINITE

    private var elapsed = 0f

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (duration < Duration.INFINITE) return true
        if (duration == Duration.INSTANT) {
            // delta time is used as a multiplier, so an instant multiplier is just 1 for the full amount.
            step(1f, engine, entity)
            return true
        }

        step(deltaTime, engine, entity)
        if (duration == Duration.INFINITE) return false
        elapsed = Math.min(duration, elapsed + deltaTime)
        return elapsed >= duration
    }

    /**
     * Called each time the action should happen.
     * @param deltaTime The current delta duration, in seconds.
     * @param engine The entity engine.
     * @param entity The entity being scripted.
     */
    abstract fun step(deltaTime: Float, engine: Engine, entity: Entity)

    override fun reset() {
        duration = Duration.INFINITE
        elapsed = 0f
    }
}