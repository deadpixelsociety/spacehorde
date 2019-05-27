package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.components.*
import com.spacehorde.scripts.impl.PlayerScript
import com.spacehorde.ships.PlayerShipMask
import com.spacehorde.weapons.DoubleBullet

class PlayerShipGenerator : ShipGenerator() {
    override fun generate(engine: Engine): Entity {
        val entity = createRandomShip(PlayerShipMask(), MathUtils.random() * 360f)
        entity.add(component<GroupMask> { mask = Groups.PLAYERS })
        entity.add(component<Weaponized> { add(DoubleBullet()) })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>())

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(200f, 300f)
            this.accelerationSpeed = MathUtils.random(400f, 600f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        entity.add(component<Scripted> {
            this.scripts.add(PlayerScript())
        })

        return entity
    }
}