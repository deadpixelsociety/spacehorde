package com.spacehorde.scene.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.components.GroupMask
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.entities.Entities
import com.spacehorde.graphics.Fonts
import com.spacehorde.scene.SceneImpl
import com.spacehorde.service.service
import com.spacehorde.systems.*

class TestScene : SceneImpl() {
    private val engine = Engine()
    private val viewport by lazy { FillViewport(600f, 600f) }
    private val uiViewport by lazy { ScreenViewport() }
    private val batch by service<SpriteBatch>()
    private val groupMapper by mapper<GroupMask>()
    private val transformMapper by mapper<Transform>()

    override fun create() {
        createSystems()

        Entities.wall(engine, -250f, -250f, 5f, 500f)
        Entities.wall(engine, 250f, -250f, 5f, 500f)
        Entities.wall(engine, -250f, -250f, 505f, 5f)
        Entities.wall(engine, -250f, 245f, 505f, 5f)

        Entities.player(engine, 0f, -200f)

        // Prime physics
        engine.getSystem(Box2DSystem::class.java).update(1f)
    }

    private fun createSystems() {
        engine.addSystem(TagSystem())
        engine.addSystem(GroupSystem())
        engine.addSystem(GraveyardSystem())
        engine.addSystem(ControlSystem())
        engine.addSystem(ScriptSystem())
        engine.addSystem(Box2DSystem())
        engine.addSystem(SpatialSystem(500f, 500f))
        engine.addSystem(RenderSystem(viewport.camera))
        if (SpaceHordeGame.DEBUG) engine.addSystem(DebugRenderSystem(viewport.camera))
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        uiViewport.update(width, height, true)
    }

    override fun update(dt: Float, tt: Float) {
        engine.update(dt)

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            engine.addEntity(Entities.player(engine, 0f, 0f))
        }
    }

    override fun draw(dt: Float, tt: Float) {
        Gdx.gl.glClearColor(.05f, .05f, .05f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val players = engine.getSystem(GroupSystem::class.java)[Groups.PLAYERS]
        var px = 0f
        var py = 0f

        players.forEach {
            val transform = transformMapper.get(it)
            px += transform.position.x
            py += transform.position.y
        }

        px /= players.size.toFloat()
        py /= players.size.toFloat()

        viewport.camera.position.set(px, py, 0f)

        viewport.apply()
        engine.getSystem(RenderSystem::class.java)?.update(dt)
        if (SpaceHordeGame.DEBUG) engine.getSystem(DebugRenderSystem::class.java)?.update(dt)

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