package com.spacehorde.components

import com.badlogic.gdx.graphics.OrthographicCamera

class Dolly : PoolableComponent() {
    var camera: OrthographicCamera? = null

    override fun reset() {
        camera = null
    }
}