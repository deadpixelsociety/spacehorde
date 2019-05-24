package com.spacehorde.service.impl

import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.spacehorde.service.SingletonServiceProvider

class ShapeRendererProvider : SingletonServiceProvider<ShapeRenderer>() {
    override val providesType: Class<ShapeRenderer> = ShapeRenderer::class.java

    override fun provideSingleton(): ShapeRenderer {
        return ShapeRenderer()
    }
}