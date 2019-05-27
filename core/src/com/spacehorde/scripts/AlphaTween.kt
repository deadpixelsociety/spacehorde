package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Interpolation
import com.spacehorde.Tween
import com.spacehorde.components.Tint
import com.spacehorde.components.mapper

class AlphaTween() : TweenScript<Float>() {
    companion object {
        val DEFAULT_TWEEN: Tween<Float> = { start, end, t -> Interpolation.linear.apply(start, end, t) }
    }

    init {
        tween = DEFAULT_TWEEN
    }

    constructor(start: Float, target: Float, time: Float = Duration.INSTANT) : this() {
        this.start = start
        this.end = target
        this.duration = time
    }

    override var start = 0f

    override var end = 0f

    private val tintMapper by mapper<Tint>()

    override fun start(engine: Engine, entity: Entity) {
        super.start(engine, entity)
        elapsed = 0f
    }

    override fun updateValue(engine: Engine, entity: Entity, value: Float) {
        val tint = tintMapper[entity] ?: return
        tint.color.a = value
    }

    override fun reset() {
        super.reset()
        start = 0f
        end = 0f
        tween = DEFAULT_TWEEN
    }
}