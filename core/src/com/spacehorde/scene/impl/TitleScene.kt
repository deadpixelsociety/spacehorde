package com.spacehorde.scene.impl

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FillViewport
import com.spacehorde.assets.asset
import com.spacehorde.config.CustomControllerMappings
import com.spacehorde.scene.SceneContainer
import com.spacehorde.scene.SceneImpl
import com.spacehorde.service.service
import de.golfgl.gdx.controllers.mapping.MappedController

class TitleScene : SceneImpl() {
    private val viewport by lazy { FillViewport(640f, 640f) }
    private val batch by service<SpriteBatch>()
    private val bg by asset<Texture>("textures/title.png")
    private val mappings by service<CustomControllerMappings>()
    private val sceneContainer by service<SceneContainer>()
    private var startWasPressed = false
    private val bgSprite by lazy {
        Sprite(bg).apply {
            setOriginCenter()
            setOriginBasedPosition(0f, 0f)
        }
    }

    override fun create() {
        super.create()
        viewport.setScreenPosition(0, 0)
        (viewport.camera as OrthographicCamera).zoom = 1.4f
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }

    override fun update(dt: Float, tt: Float) {
        val controller = Controllers.getControllers().firstOrNull() ?: return
        val mappedController = MappedController(controller, mappings)

        val start = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_START)
        if (start && !startWasPressed) {
            startWasPressed = true
            sceneContainer.add(ArenaScene())
        }
    }

    override fun draw(dt: Float, tt: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        batch.begin()
        bgSprite.draw(batch)
        batch.end()
    }
}