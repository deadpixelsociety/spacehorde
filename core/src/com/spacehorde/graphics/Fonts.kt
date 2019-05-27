package com.spacehorde.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.spacehorde.using

object Fonts : LifecycleListener {
    lateinit var text: BitmapFont
    lateinit var scoreUI: BitmapFont
    lateinit var scoreText: BitmapFont
    val glyphLayout = GlyphLayout()

    fun load() {
        FreeTypeFontGenerator(Gdx.files.internal("fonts\\visitor2.ttf")).using {
            val param = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 12
                this.genMipMaps = true
                this.magFilter = Texture.TextureFilter.MipMapLinearLinear
                this.minFilter = Texture.TextureFilter.MipMapLinearLinear
            }

            text = it.generateFont(param)
        }

        FreeTypeFontGenerator(Gdx.files.internal("fonts\\visitor2.ttf")).using {
            val param = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 32
                this.genMipMaps = true
                this.magFilter = Texture.TextureFilter.MipMapLinearLinear
                this.minFilter = Texture.TextureFilter.MipMapLinearLinear
            }

            scoreUI = it.generateFont(param)
        }

        FreeTypeFontGenerator(Gdx.files.internal("fonts\\visitor2.ttf")).using {
            val param = FreeTypeFontGenerator.FreeTypeFontParameter().apply {
                this.size = 28
                this.genMipMaps = true
                this.magFilter = Texture.TextureFilter.MipMapLinearLinear
                this.minFilter = Texture.TextureFilter.MipMapLinearLinear
                this.borderWidth = 1f
                this.color = Color.WHITE
                this.borderColor = Color.LIGHT_GRAY
            }

            scoreText = it.generateFont(param)
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