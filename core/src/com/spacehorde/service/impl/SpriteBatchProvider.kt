package com.spacehorde.service.impl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.spacehorde.service.SingletonServiceProvider

class SpriteBatchProvider : SingletonServiceProvider<SpriteBatch>() {
    override val providesType: Class<SpriteBatch> = SpriteBatch::class.java

    override fun provideSingleton(): SpriteBatch {
        return SpriteBatch()
    }
}