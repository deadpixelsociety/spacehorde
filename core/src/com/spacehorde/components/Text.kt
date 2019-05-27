package com.spacehorde.components

import com.badlogic.gdx.math.Vector2
import com.spacehorde.graphics.Fonts

class Text : PoolableComponent() {
    var text = ""
    var font = Fonts.text
    val offset = Vector2()

    override fun reset() {
        text = ""
        font = Fonts.text
    }
}