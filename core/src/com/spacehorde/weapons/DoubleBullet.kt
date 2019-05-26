package com.spacehorde.weapons

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Meta
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.entities.Entities
import com.spacehorde.ships.ShipColor

class DoubleBullet : WeaponDefImpl() {
    companion object {
        private const val BULLET_SIZE = 5f
        private const val BULLET_OFFSET = 6f
    }

    private val physicsMapper by mapper<Box2DPhysics>()
    private val transformMapper by mapper<Transform>()
    private val metaMapper by mapper<Meta>()
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun fire(axis: Vector2, engine: Engine, owner: Entity) {
        val playerPhysics = physicsMapper.get(owner) ?: return
        val playerTransform = transformMapper.get(owner) ?: return
        val playerMeta = metaMapper.get(owner) ?: return

        v0.set(playerTransform.position).sub(BULLET_SIZE * .5f, BULLET_SIZE * .5f).add(playerTransform.origin)
        val color = playerMeta.get<ShipColor>("ShipColor").cockpitColor

        addBullet(engine, axis, color, -1, playerPhysics)
        addBullet(engine, axis, color, 1, playerPhysics)
    }

    private fun addBullet(engine: Engine, axis: Vector2, color: Color, dir: Int, playerPhysics: Box2DPhysics) {
        if (dir == -1) v1.set(-axis.y, axis.x).nor().scl(BULLET_OFFSET) else v1.set(axis.y, -axis.x).nor().scl(BULLET_OFFSET)
        v1.add(v0).add(BULLET_SIZE * .5f, BULLET_SIZE * .5f)

        val bullet = Entities.bullet(engine, v1.x, v1.y, BULLET_SIZE, BULLET_SIZE, color)
        val physics = physicsMapper.get(bullet)

        physics.initialAngle = axis.angleRad() + (MathUtils.random(-.15f, .15f))
        physics.initialSpeed = (playerPhysics.body?.linearVelocity?.len() ?: 0f) + physics.maxSpeed

        engine.addEntity(bullet)
    }
}