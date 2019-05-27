package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.scripts.Script

class ShrapnelScript : Script() {
    private val physicsMapper by mapper<Box2DPhysics>()
    private val groupMapper by mapper<GroupMask>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val physics = physicsMapper.get(entity)


        if (physics.collision) {
            val collidedGroup = groupMapper.get(physics.collided)
            if (collidedGroup.match(Groups.WALLS)) {
                entity.add(component<Dead>(engine))
                // TODO: particles, sounds!
            } else if (collidedGroup.match(Groups.PLAYERS)) {
                physics.collided?.add(component<Dying>(engine))
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
            // TODO: particles, sounds!
        }
    }
}