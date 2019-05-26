package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.spacehorde.Tween
import com.spacehorde.components.Tint
import com.spacehorde.components.mapper

class ColorTween() : TweenScript<Color>() {
    companion object {
        val DEFAULT_TWEEN: Tween<Color> = { start, end, t -> c0.set(start).lerp(end, t) }
        private val c0 = Color(Color.WHITE)
    }

    init {
        tween = DEFAULT_TWEEN
    }

    constructor(start: Color, target: Color, time: Float = Duration.INSTANT) : this() {
        this.start.set(start)
        this.end.set(target)
        this.duration = time
    }

    override var start = Color(Color.WHITE)

    override var end = Color(Color.WHITE)

    private val tintMapper by mapper<Tint>()

    override fun start(engine: Engine, entity: Entity) {
        super.start(engine, entity)
        elapsed = 0f
    }

    override fun updateValue(engine: Engine, entity: Entity, value: Color) {
        val tint = tintMapper[entity] ?: return
        tint.color.set(value)
    }

    override fun reset() {
        super.reset()
        start.set(Color.WHITE)
        end.set(Color.WHITE)
        c0.set(Color.WHITE)
        tween = DEFAULT_TWEEN
    }
}