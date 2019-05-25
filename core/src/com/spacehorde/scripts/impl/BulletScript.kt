package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.components.*
import com.spacehorde.scripts.Script

class BulletScript : Script() {
    private val collisionMapper by mapper<Collision>()
    private val groupMapper by mapper<GroupMask>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val collision = collisionMapper.get(entity)

        if (collision.collision) {
            val collidedGroup = groupMapper.get(collision.collided)
            if (collidedGroup.has(GroupMask.WALLS)) {
                // collide with walls
                entity.add(component<Dead>())
            } else if (collidedGroup.has(GroupMask.ENEMIES)) {
                // collide with enemies
                collision.collided?.add(component<Dead>())
            }
        }

        return false
    }
}