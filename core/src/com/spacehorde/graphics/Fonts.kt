package com.spacehorde.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.spacehorde.using

object Fonts : LifecycleListener {
    lateinit var text: BitmapFont

    fun load() {
        FreeTypeFontGenerator(Gdx.files.internal("fonts\\visitor2.ttf")).using {
            val param = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 10
                this.genMipMaps = true
                this.magFilter = Texture.TextureFilter.MipMapLinearLinear
                this.minFilter = Texture.TextureFilter.MipMapLinearLinear
            }

            text = it.generateFont(param)
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        text.dispose()
    }
}