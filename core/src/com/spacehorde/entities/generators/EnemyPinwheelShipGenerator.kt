package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.Rotate
import com.spacehorde.scripts.impl.EnemyPinwheelScript

class EnemyPinwheelShipGenerator : ShipGenerator() {
    private val texture by asset<Texture>("textures/enemy_pinwheel.png")

    override fun generate(engine: Engine): Entity {
        val entity = createTextureShip(texture)
        entity.add(component<GroupMask> { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>())

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(200f, 300f)
            this.accelerationSpeed = MathUtils.random(400f, 600f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        entity.add(component<Scripted> {
            this.scripts.add(Rotate(720f))
            this.scripts.add(EnemyPinwheelScript())
        })

        return entity
    }
}