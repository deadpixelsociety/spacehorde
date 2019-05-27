package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
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


        val entity = createTextureShip(engine, texture)
        entity.add(component<GroupMask>(engine) { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>(engine))

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(100f, 150f)
            this.accelerationSpeed = MathUtils.random(200f, 400f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        entity.add(component<Scripted>(engine) {
            scripts.add(Rotate(MathUtils.random(45f, 180f)))
            scripts.add(LoopScript().apply {
                this.scripts.add(ScaleTween(1.25f, 1.25f, MathUtils.random(.25f, 1.25f)))
                this.scripts.add(ScaleTween(1f, 1f, MathUtils.random(.25f, 1.25f)))
            })

            scripts.add(EnemyCircleScript())
        })

        entity.add(component<ScoreValue>(engine) { value = 25 })
        entity.add(component<Meta>(engine) {
            this.put("ShipColor", Color(1f, 102f / 255f, 168f / 255f, 1f))
        })

        return entity
    }
}