package com.spacehorde

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.spacehorde.config.CustomControllerMappings
import com.spacehorde.graphics.Fonts
import com.spacehorde.scene.SceneContainer
import com.spacehorde.service.ServiceContainer
import com.spacehorde.service.impl.SceneContainerProvider
import com.spacehorde.service.impl.ShapeRendererProvider
import com.spacehorde.service.impl.SpriteBatchProvider
import com.spacehorde.service.registerService
import com.spacehorde.service.service
import com.spacehorde.states.TestScene

class SpaceHordeGame : ApplicationAdapter() {
    companion object {
        private const val DT = .01f
        private const val MAX_DT = 1f / 60f
    }

    private val sceneContainer by service<SceneContainer>()
    private var accumulator = 0f
    private var tt = 0f

    override fun create() {
        Gdx.app.addLifecycleListener(ServiceContainer)
        Gdx.app.addLifecycleListener(Fonts)

        Fonts.load()

        registerService(AssetManager())
        registerService(SpriteBatchProvider())
        registerService(ShapeRendererProvider())
        registerService(SceneContainerProvider())
        registerService(CustomControllerMappings())

        sceneContainer.add(TestScene())
    }

    override fun pause() {
        sceneContainer.pause()
    }

    override fun resume() {
        sceneContainer.resume()
    }

    override fun resize(width: Int, height: Int) {
        sceneContainer.resize(width, height)
    }

    override fun render() {
        val realDelta = Gdx.graphics.deltaTime
        val dt = Math.min(realDelta, MAX_DT)
        accumulator += dt

        while (accumulator >= DT) {
            sceneContainer.update(DT, tt)
            accumulator -= DT
            tt += DT
        }

        sceneContainer.draw(realDelta, tt)
    }
}
