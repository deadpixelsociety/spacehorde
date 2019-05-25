package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Rectangle
import com.spacehorde.components.*
import com.spacehorde.scripts.Script

class WallColliderScript : Script() {
    private val collisionMapper by mapper<Collision>()
    private val groupMapper by mapper<GroupMask>()
    private val transformMapper by mapper<Transform>()
    private val sizeMapper by mapper<Size>()
    private val physicsMapper by mapper<Physics>()
    private val r0 = Rectangle()
    private val r1 = Rectangle()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val collision = collisionMapper.get(entity)

        if (collision.collision) {
            val collidedGroup = groupMapper.get(collision.collided)
            if (collidedGroup.has(GroupMask.WALLS)) {
                val transform = transformMapper.get(entity)
                val size = sizeMapper.get(entity)
                val physics = physicsMapper.get(entity)
                val collidedTransform = transformMapper.get(entity)
                val collidedSize = sizeMapper.get(entity)
                val collidedPhysics = physicsMapper.get(entity)

                r0.setCenter(transform.position.x + transform.origin.x, transform.position.y + transform.origin.y)
                r0.setSize(size.width, size.height)

                r1.setCenter(
                        collidedTransform.position.x + collidedTransform.origin.x,
                        collidedTransform.position.y + collidedTransform.origin.y
                )
                r1.setSize(collidedSize.width, collidedSize.height)


            }
        }

        return false
    }
}