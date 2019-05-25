package com.spacehorde.components

import com.badlogic.gdx.graphics.Color

class Tint : PoolableComponent() {
    val color = Color(Color.WHITE)

    override fun reset() {
        color.set(Color.WHITE)
    }
}