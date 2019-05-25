package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

data class Transform(
    val position: Vector2,
    val origin: Vector2,
    val scale: Vector2 = Vector2(1f, 1f),
    val heading: Vector2 = Vector2(0f, 1f),
    var rotation: Float = 0f,
    var width: Float = 1f,
    var height: Float = 1f
) : Component