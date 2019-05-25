package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

data class Physics(
    val velocity: Vector2 = Vector2(),
    val acceleration: Vector2 = Vector2(),
    var maxSpeed: Float = 0f,
    var rotationSpeed: Float = 0f
) : Component