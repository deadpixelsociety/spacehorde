package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Interpolation
import com.spacehorde.Tween
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper

/**
 * Smoothly rotates the entity to a end rotation over a period of duration. Requires the Transform component.
 */
class RotateTween() : TweenScript<Float>() {
    companion object {
        // Simple linear tween
        val DEFAULT_TWEEN: Tween<Float> = { start, end, t ->
            Interpolation.linear.apply(start, end, t)
        }
    }

    init {
        tween = DEFAULT_TWEEN
    }

    constructor(end: Float, duration: Float = Duration.INSTANT, tween: Tween<Float> = DEFAULT_TWEEN) : this() {
        this.end = end
        this.duration = duration
        this.tween = tween
    }

    override var start = 0f

    override var end = 0f

    private val transformMapper by mapper<Transform>()

    override fun start(engine: Engine, entity: Entity) {
        val transform = transformMapper[entity] ?: return
        start = transform.angle
    }

    override fun updateValue(engine: Engine, entity: Entity, value: Float) {
        val transform = transformMapper[entity] ?: return
        transform.angle = value
    }

    override fun reset() {
        super.reset()
        start = 0f
        end = 0f
        tween = DEFAULT_TWEEN
    }
}