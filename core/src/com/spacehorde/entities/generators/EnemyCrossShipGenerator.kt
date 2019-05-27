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
import com.spacehorde.scripts.ScaleTween
import com.spacehorde.scripts.impl.EnemyCrossScript

class EnemyCrossShipGenerator : ShipGenerator() {
    private val texture by asset<Texture>("textures/enemy_cross.png")

    override fun generate(engine: Engine): Entity {
        val entity = createTextureShip(texture)
        entity.add(component<GroupMask> { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>())

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(400f, 600f)
            this.accelerationSpeed = MathUtils.random(400f, 600f)
            this.rotationSpeed = MathUtils.random(.09f, .18f)
        }

        entity.add(component<Scripted> {
            //this.scripts.add(Rotate(45f))
            this.scripts.add(LoopScript().apply {
                this.scripts.add(ScaleTween(1.5f, 1f, MathUtils.random(.25f, 1f)))
                this.scripts.add(ScaleTween(1f, 1f, MathUtils.random(.25f, 1f)))
                this.scripts.add(ScaleTween(1f, 1.5f, MathUtils.random(.25f, 1f)))
                this.scripts.add(ScaleTween(1f, 1f, MathUtils.random(.25f, 1f)))
            })

            this.scripts.add(EnemyCrossScript())
        })

        return entity
    }
}