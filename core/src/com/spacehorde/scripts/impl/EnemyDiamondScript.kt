package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.spacehorde.Groups
import com.spacehorde.systems.GroupSystem
import com.spacehorde.systems.SpatialSystem

class EnemyDiamondScript : EnemyScript() {
    private val accel = Vector2()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        super.update(deltaTime, engine, entity)
        evadeAndChasePlayerAndAvoidBullets(engine, entity)
        return false
    }

    private fun evadeAndChasePlayerAndAvoidBullets(engine: Engine, entity: Entity) {
        val groupSystem = engine.getSystem(GroupSystem::class.java)
        val players = groupSystem[Groups.PLAYERS]
        val bullets = groupSystem[Groups.BULLETS]
        val physics = physicsMapper.get(entity)

        v0.set(0f, 0f)
        players.forEach { player ->
            val playerPhysics = physicsMapper.get(player)
            val body = playerPhysics.body
            if (body != null) v0.add(body.worldCenter)
        }

        if (players.isNotEmpty()) v0.set(v0.x / players.size, v0.y / players.size)

        accel.set(0f, 0f)
        if (v0.x != 0f && v0.y != 0f) {
            val body = physics.body
            if (body != null) {
                val distance = v1.set(v0).sub(body.worldCenter).len()
                if (distance <= 100f) {
                    v1.set(body.worldCenter).sub(v0).nor().scl(physics.maxSpeed)
                    accel.add(v1)
                } else {
                    v1.set(v0).sub(body.worldCenter).nor().scl(physics.maxSpeed)
                    accel.add(v1)
                }

                if (bullets.isNotEmpty()) {
                    val nearestBullet = bullets.minBy { bullet ->
                        val bulletPhysics = physicsMapper.get(bullet)
                        val bulletBody = bulletPhysics.body
                        if (bulletBody != null) {
                            v1.set(body.worldCenter).sub(bulletBody.worldCenter).len()
                        } else {
                            Float.MAX_VALUE
                        }
                    }

                    val bulletPhysics = physicsMapper.get(nearestBullet)
                    val bulletBody = bulletPhysics.body
                    if (bulletBody != null) {
                        val vectorTo = v1.set(body.worldCenter).sub(bulletBody.worldCenter)
                        if (vectorTo.len() <= 150f) {
                            accel.add(vectorTo.nor().scl(1000f))
                        }
                    }
                }
            }
        }

        physics.acceleration.set(accel)
    }
}