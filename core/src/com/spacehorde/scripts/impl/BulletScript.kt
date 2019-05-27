package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.entities.Entities
import com.spacehorde.scripts.Script

class BulletScript : Script() {
    private val transformMapper by mapper<Transform>()
    private val tintMapper by mapper<Tint>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val groupMapper by mapper<GroupMask>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        val physics = physicsMapper.get(entity)

        if (physics.collision) {
            val collidedGroup = groupMapper.get(physics.collided)
            if (collidedGroup.match(Groups.WALLS)) {
                die(engine, entity)
            } else if (collidedGroup.match(Groups.ENEMIES)) {
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
            val transform = transformMapper.get(entity)
            val tint = tintMapper.get(entity)

            entity.add(component<Dead>(engine))
            Entities.pop(
                    engine,
                    transform.position.x + transform.origin.x,
                    transform.position.y + transform.origin.y,
                    tint?.color ?: Color.WHITE
            )
        }
    }
}