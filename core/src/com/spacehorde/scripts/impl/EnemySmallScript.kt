package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class EnemySmallScript : EnemyScript() {
    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        super.update(deltaTime, engine, entity)
        chasePlayer(engine, entity)

        val transform = transformMapper.get(entity)
        val physics = physicsMapper.get(entity)

        val body = physics.body
        if (body != null) {
            transform.angle = body.linearVelocity.angle() - 90f
        }

        return false
    }
}