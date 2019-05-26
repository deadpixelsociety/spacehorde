package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.scripts.Script
import com.spacehorde.systems.GroupSystem

class EnemyCircleScript : Script() {
    private val physicsMapper by mapper<Box2DPhysics>()
    private val groupMapper by mapper<GroupMask>()
    private val transformMapper by mapper<Transform>()
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val groupSystem = engine.getSystem(GroupSystem::class.java)
        val players = groupSystem[Groups.PLAYERS]
        val physics = physicsMapper.get(entity)

        if (physics.collision) {
            val collidedGroup = groupMapper.get(physics.collided)
            if (collidedGroup.match(Groups.BULLETS) || collidedGroup.match(Groups.PLAYERS)) {
                println("collided with ${physics.collided}")
                physics.collided?.add(component<Dead>())
                return false
            }
        }

        v0.set(0f, 0f)
        players.forEach { player ->
            val playerTransform = transformMapper.get(player)
            v0.add(playerTransform.position)
        }

        if (players.isNotEmpty()) v0.set(v0.x / players.size, v0.y / players.size)

        val transform = transformMapper.get(entity)
        v1.set(v0).sub(transform.position).nor().scl(physics.maxSpeed)
        physics.body?.linearVelocity = v1

        return false
    }
}