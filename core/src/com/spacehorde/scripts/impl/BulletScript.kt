package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.scripts.Script

class BulletScript : Script() {
    private val physicsMapper by mapper<Box2DPhysics>()
    private val groupMapper by mapper<GroupMask>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val physics = physicsMapper.get(entity)

        if (physics.collision) {
            val collidedGroup = groupMapper.get(physics.collided)
            if (collidedGroup.match(Groups.WALLS)) {
                entity.add(component<Dead>())
            } else if (collidedGroup.match(Groups.ENEMIES)) {
                // collide with enemies
                physics.collided?.add(component<Dead>())
            }
        }

        return false
    }
}