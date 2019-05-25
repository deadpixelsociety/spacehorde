package com.spacehorde.components

import com.badlogic.gdx.graphics.g2d.Sprite

class RenderSprite : PoolableComponent() {
    var sprite: Sprite? = null

    override fun reset() {
        sprite = null
    }
}