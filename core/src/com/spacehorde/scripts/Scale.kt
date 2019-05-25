package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper

/**
 * Expands an entity's scale by a set amount per update. Requires the Transform component.
 */
class Scale() : DurationScript() {
    constructor(x: Float, y: Float, duration: Float = Duration.INFINITE) : this() {
        amount.set(x, y)
        this.duration = duration
    }

    /**
     * The amount to move per second.
     */
    val amount = Vector2()

    private val transformMapper by mapper<Transform>()

    override fun step(deltaTime: Float, engine: Engine, entity: Entity) {
        val transform = transformMapper[entity] ?: return
        transform.scale.add(amount.x * deltaTime, amount.y * deltaTime)
    }

    override fun reset() {
        super.reset()
        amount.set(0f, 0f)
    }
}