package com.spacehorde.states

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.spacehorde.entities.Entities
import com.spacehorde.graphics.Fonts
import com.spacehorde.scene.SceneImpl
import com.spacehorde.service.service
import com.spacehorde.systems.*

class TestScene : SceneImpl() {
    private val engine = Engine()
    private val viewport by lazy { FillViewport(450f, 450f) }
    private val uiViewport by lazy { ScreenViewport() }
    private val batch by service<SpriteBatch>()

    override fun create() {
        createSystems()
        engine.addEntity(Entities.createPlayerShip(0f, 0f))
    }

    private fun createSystems() {
        engine.addSystem(TagSystem())
        engine.addSystem(GroupSystem())
        engine.addSystem(ControlSystem())
        engine.addSystem(ScriptSystem())
        engine.addSystem(PhysicsSystem())
        engine.addSystem(RenderSystem(viewport.camera))
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        uiViewport.update(width, height, true)
    }

    override fun update(dt: Float, tt: Float) {
        engine.update(dt)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            engine.addEntity(Entities.createPlayerShip(0f, 0f))
        }
    }

    override fun draw(dt: Float, tt: Float) {
        Gdx.gl.glClearColor(.05f, .05f, .05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        viewport.apply()
        engine.getSystem(RenderSystem::class.java)?.update(dt)

        renderUI()
    }

    override fun dispose() {
    }

    private fun renderUI() {
        uiViewport.apply()
        batch.projectionMatrix = uiViewport.camera.combined
        batch.begin()

        Fonts.text.draw(batch, "Testing...", 0f, Fonts.text.lineHeight)

        batch.end()
    }
}