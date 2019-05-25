package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.spacehorde.Tween
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper

class ScaleTween() : TweenScript<Vector2>() {
    companion object {
        // Simple linear tween
        val DEFAULT_TWEEN: Tween<Vector2> = { start, end, t ->
            v0.set(start).nor().setLength(Interpolation.linear.apply(start.len(), end.len(), t))
        }

        private val v0 = Vector2()
    }

    init {
        tween = DEFAULT_TWEEN
    }

    constructor(x: Float, y: Float, duration: Float = Duration.INSTANT, tween: Tween<Vector2> = DEFAULT_TWEEN) : this() {
        end.set(x, y)
        this.duration = duration
        this.tween = tween
    }

    override var start = Vector2()
    override var end = Vector2()

    private val transformMapper by mapper<Transform>()

    override fun start(engine: Engine, entity: Entity) {
        val transform = transformMapper[entity] ?: return
        start.set(transform.scale)
    }

    override fun updateValue(engine: Engine, entity: Entity, value: Vector2) {
        val transform = transformMapper[entity] ?: return
        transform.scale.set(value)
    }

    override fun reset() {
        super.reset()
        start.set(0f, 0f)
        end.set(0f, 0f)
        tween = DEFAULT_TWEEN
    }
}