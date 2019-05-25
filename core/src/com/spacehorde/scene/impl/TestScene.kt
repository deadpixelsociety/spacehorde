package com.spacehorde.scene.impl

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.spacehorde.scene.SceneContainer
import com.spacehorde.scene.SceneImpl
import com.spacehorde.service.service
import com.spacehorde.ships.PlayerShipMask

class TestScene : SceneImpl() {
    private val spriteBatch by service<SpriteBatch>()
    private val sceneContainer by service<SceneContainer>()

    override fun create() {
        val mask = PlayerShipMask().generateRandomFullMask()
    }
}