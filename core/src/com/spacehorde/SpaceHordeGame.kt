package com.spacehorde

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.spacehorde.service.ServiceContainer
import com.spacehorde.service.impl.ShapeRendererProvider
import com.spacehorde.service.impl.SpriteBatchProvider

class SpaceHordeGame : ApplicationAdapter() {
    override fun create() {
        Gdx.app.addLifecycleListener(ServiceContainer)

        ServiceContainer.register(SpriteBatchProvider())
        ServiceContainer.register(ShapeRendererProvider())
    }

    override fun render() {
    }

    override fun dispose() {
    }
}
