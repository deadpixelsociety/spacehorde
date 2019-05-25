package com.spacehorde.components

import com.badlogic.gdx.math.Vector2

class Physics : PoolableComponent() {
    val velocity: Vector2 = Vector2()
    val acceleration: Vector2 = Vector2()
    var maxSpeed = 0f
    var rotationSpeed = 0f
    var accelerationSpeed = 0f
    var frictionless = false

    override fun reset() {
        velocity.set(0f, 0f)
        acceleration.set(0f, 0f)
        maxSpeed = 0f
        rotationSpeed = 0f
        frictionless = false
    }
}