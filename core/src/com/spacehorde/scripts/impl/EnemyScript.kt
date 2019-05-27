package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.spacehorde.AudioManager
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.entities.Entities
import com.spacehorde.scoring.ScoreKeeper
import com.spacehorde.scripts.Script
import com.spacehorde.systems.GroupSystem

abstract class EnemyScript : Script() {
    protected val physicsMapper by mapper<Box2DPhysics>()
    protected val groupMapper by mapper<GroupMask>()
    protected val metaMapper by mapper<Meta>()
    protected val transformMapper by mapper<Transform>()
    protected val v0 = Vector2()
    protected val v1 = Vector2()
    private val scoreMapper by mapper<ScoreValue>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {

        val physics = physicsMapper.get(entity)

        if (physics != null && physics.collision) {
            val collidedGroup = groupMapper.get(physics.collided)
            if (collidedGroup.match(Groups.BULLETS) || collidedGroup.match(Groups.PLAYERS)) {
                physics.collided?.add(component<Dying>(engine))
                return false
            }
        }

        if (entity.has<Dying>()) {
            die(engine, entity)
        }

        return false
    }

    private fun die(engine: Engine, entity: Entity) {
        if (!entity.has<Dead>()) {
            entity.add(component<Dead>(engine))
            addScore(engine, entity)
            onDie(engine, entity)
            AudioManager.playExplosion()
        }
    }

    protected open fun addScore(engine: Engine, entity: Entity) {

        val scoreValue = scoreMapper.get(entity)
        val transform = transformMapper.get(entity)
        val meta = metaMapper.get(entity)

        var color = Color.WHITE
        if (meta != null) color = meta.get("ShipColor")

        val score = scoreValue?.value ?: 0
        ScoreKeeper.score += score
        Entities.scoreText(engine, transform.position.x + transform.origin.x, transform.position.y + transform.origin.y, score, color)
    }

    protected open fun onDie(engine: Engine, entity: Entity) {
        val transform = transformMapper.get(entity)
        val meta = metaMapper.get(entity)

        var color = Color.WHITE
        if (meta != null) color = meta.get("ShipColor")

        Entities.explosion(engine, transform.position.x + transform.origin.x, transform.position.y + transform.origin.y, color)
    }

    protected fun chasePlayer(engine: Engine, entity: Entity) {
        val groupSystem = engine.getSystem(GroupSystem::class.java)
        val players = groupSystem[Groups.PLAYERS]
        val physics = physicsMapper.get(entity)

        v0.set(0f, 0f)
        players.forEach { player ->
            val playerPhysics = physicsMapper.get(player)
            val body = playerPhysics.body
            if (body != null) v0.add(body.worldCenter)
        }

        if (players.isNotEmpty()) v0.set(v0.x / players.size, v0.y / players.size)

        if (v0.x != 0f && v0.y != 0f) {
            val body = physics.body
            if (body != null) {
                v1.set(v0).sub(body.worldCenter).nor().scl(physics.maxSpeed)
                physics.acceleration.set(v1)
            } else {
                physics.acceleration.set(0f, 0f)
            }
        } else {
            physics.acceleration.set(0f, 0f)
        }
    }

    protected fun evadePlayer(engine: Engine, entity: Entity) {
        val groupSystem = engine.getSystem(GroupSystem::class.java)
        val players = groupSystem[Groups.PLAYERS]
        val physics = physicsMapper.get(entity)

        v0.set(0f, 0f)
        players.forEach { player ->
            val playerPhysics = physicsMapper.get(player)
            val body = playerPhysics.body
            if (body != null) v0.add(body.worldCenter)
        }

        if (players.isNotEmpty()) v0.set(v0.x / players.size, v0.y / players.size)

        if (v0.x != 0f && v0.y != 0f) {
            val body = physics.body
            if (body != null) {
                val distance = v1.set(v0).sub(body.worldCenter).len()
                if (distance <= 100f) {
                    v1.set(body.worldCenter).sub(v0).nor().scl(physics.maxSpeed)
                    physics.acceleration.set(v1)
                } else {
                    physics.acceleration.set(0f, 0f)
                }
            } else {
                physics.acceleration.set(0f, 0f)
            }
        } else {
            physics.acceleration.set(0f, 0f)
        }
    }

    protected fun evadeAndChasePlayer(engine: Engine, entity: Entity) {
        val groupSystem = engine.getSystem(GroupSystem::class.java)
        val players = groupSystem[Groups.PLAYERS]
        val physics = physicsMapper.get(entity)

        v0.set(0f, 0f)
        players.forEach { player ->
            val playerPhysics = physicsMapper.get(player)
            val body = playerPhysics.body
            if (body != null) v0.add(body.worldCenter)
        }

        if (players.isNotEmpty()) v0.set(v0.x / players.size, v0.y / players.size)

        if (v0.x != 0f && v0.y != 0f) {
            val body = physics.body
            if (body != null) {
                val distance = v1.set(v0).sub(body.worldCenter).len()
                if (distance <= 100f) {
                    v1.set(body.worldCenter).sub(v0).nor().scl(physics.maxSpeed)
                    physics.acceleration.set(v1)
                } else {
                    v1.set(v0).sub(body.worldCenter).nor().scl(physics.maxSpeed)
                    physics.acceleration.set(v1)
                }
            } else {
                physics.acceleration.set(0f, 0f)
            }
        } else {
            physics.acceleration.set(0f, 0f)
        }
    }
}