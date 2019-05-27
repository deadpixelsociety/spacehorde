package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.spacehorde.Groups
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.scripts.Script
import com.spacehorde.systems.GroupSystem

class DollyScript : Script() {
    private val transformMapper by mapper<Transform>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val playerPos = Vector2()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        calcPlayer(engine)
        val transform = transformMapper.get(entity)
        transform.position.set(playerPos)
        return false
    }

    private fun calcPlayer(engine: Engine) {
        val players = engine.getSystem(GroupSystem::class.java)[Groups.PLAYERS]
        var px = 0f
        var py = 0f

        players.forEach {
            val physics = physicsMapper.get(it)
            val body = physics.body
            if (body != null) {
                px += body.worldCenter.x
                py += body.worldCenter.y
            }
        }

        if (players.isNotEmpty()) {
            px /= players.size.toFloat()
            py /= players.size.toFloat()
        }

        playerPos.set(px, py)
    }
}