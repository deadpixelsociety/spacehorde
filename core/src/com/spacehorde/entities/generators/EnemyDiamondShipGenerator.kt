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
import com.spacehorde.scripts.Rotate
import com.spacehorde.scripts.impl.EnemyDiamondScript

class EnemyDiamondShipGenerator : ShipGenerator() {
    private val texture by asset<Texture>("textures/enemy_diamond.png")

    override fun generate(engine: Engine): Entity {

        val entity = createTextureShip(engine, texture)
        entity.add(component<GroupMask>(engine) { mask = Groups.ENEMIES })
        if (SpaceHordeGame.DEBUG) entity.add(component<Debug>(engine))

        entity.getComponent(Box2DPhysics::class.java).apply {
            this.maxSpeed = MathUtils.random(200f, 300f)
            this.accelerationSpeed = MathUtils.random(400f, 600f)
            this.rotationSpeed = MathUtils.random(.02f, .09f)
        }

        entity.add(component<Scripted>(engine) {
            this.scripts.add(Rotate(MathUtils.random(90f, 720f)))
            this.scripts.add(EnemyDiamondScript())
        })

        entity.add(component<ScoreValue>(engine) { value = 100 })
        entity.add(component<Meta>(engine) {
            this.put("ShipColor", Color(206f / 255f, 255f / 255f, 102f / 255f, 1f))
        })

        return entity
    }
}