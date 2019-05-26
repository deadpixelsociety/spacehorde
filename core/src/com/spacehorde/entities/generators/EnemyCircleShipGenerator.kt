package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.LoopScript
import com.spacehorde.scripts.Rotate
import com.spacehorde.scripts.ScaleTween
import com.spacehorde.scripts.impl.EnemyCircleScript

class EnemyCircleShipGenerator : ShipGenerator() {
    private val texture by asset<Texture>("textures/enemy_circle.png")

    override fun generate(engine: Engine): Entity {
        val entity = createTextureShip(texture)
        entity.add(component<GroupMask> { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>())

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(25f, 50f)
            this.accelerationSpeed = MathUtils.random(100f, 300f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        entity.add(component<Scripted> {
            scripts.add(Rotate(MathUtils.random(45f, 180f)))
            scripts.add(LoopScript().apply {
                this.scripts.add(ScaleTween(1.25f, 1.25f, MathUtils.random(.25f, 1.25f)))
                this.scripts.add(ScaleTween(1f, 1f, MathUtils.random(.25f, 1.25f)))
            })

            scripts.add(EnemyCircleScript())
        })

        return entity
    }
}