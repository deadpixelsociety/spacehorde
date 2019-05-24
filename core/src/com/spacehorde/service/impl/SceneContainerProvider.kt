package com.spacehorde.service.impl

import com.spacehorde.scene.SceneContainer
import com.spacehorde.service.SingletonServiceProvider

class SceneContainerProvider : SingletonServiceProvider<SceneContainer>() {
    override val providesType = SceneContainer::class.java

    override fun provideSingleton(): SceneContainer {
        return SceneContainer()
    }
}