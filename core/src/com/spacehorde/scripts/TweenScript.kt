package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

abstract class TweenScript<T> : Script() {
    abstract var start: T

    abstract var end: T

    var duration = Duration.INSTANT

    var tween = { _: T, end: T, _: Float -> end }

    private var elapsed = 0f

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (duration < Duration.INFINITE) return true
        if (duration == Duration.INSTANT) {
            updateValue(engine, entity, end)
            return true
        }

        val alpha = Math.min(duration, elapsed) / duration
        updateValue(engine, entity, tween(start, end, alpha))
        elapsed = Math.min(duration, elapsed + deltaTime)
        val finished = elapsed >= duration
        if (finished) updateValue(engine, entity, end)

        return finished
    }

    abstract fun updateValue(engine: Engine, entity: Entity, value: T)

    override fun reset() {
        elapsed = 0f
        duration = Duration.INSTANT
    }
}