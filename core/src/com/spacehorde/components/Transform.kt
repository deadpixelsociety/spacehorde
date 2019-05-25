package com.spacehorde.components

import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class Transform : PoolableComponent() {
    val position = Vector2()
    val origin = Vector2()
    val scale = Vector2(1f, 1f)
    val heading = Vector2(0f, 1f)
    var angle = 0f

    fun center() = Vector2(position.x + origin.x, position.y + origin.y)

    fun mat4() = Matrix4()
            .scale(scale.x, scale.y, 0f)
            .translate(-origin.x, -origin.y, 0f)
            .rotate(Vector3(0f, 0f, 1f), angle)
            .translate(position.x, position.y, 0f)

    fun mat3() = Matrix3()
            .scale(scale.x, scale.y)
            .translate(-origin.x, -origin.y)
            .rotate(angle)
            .translate(position.x, position.y)

    override fun reset() {
        position.set(0f, 0f)
        origin.set(0f, 0f)
        scale.set(1f, 1f)
        heading.set(0f, 1f)
        angle = 0f
    }
}