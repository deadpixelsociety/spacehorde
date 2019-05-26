package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.assets.asset
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Debug
import com.spacehorde.components.GroupMask
import com.spacehorde.components.component

class EnemyDiamondShipGenerator : ShipGenerator() {
    private val texture by asset<Texture>("textures/enemy_diamond.png")

    override fun generate(engine: Engine): Entity {
        val entity = createTextureShip(texture)
        entity.add(component<GroupMask> { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>())

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(200f, 300f)
            this.accelerationSpeed = MathUtils.random(400f, 600f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        return entity
    }
}