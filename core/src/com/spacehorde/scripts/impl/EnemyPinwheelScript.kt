package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Size
import com.spacehorde.components.mapper
import com.spacehorde.entities.Entities

class EnemyPinwheelScript : EnemyScript() {
    companion object {
        private const val SHRAPNEL_COUNT = 6
    }

    private val sizeMapper by mapper<Size>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        super.update(deltaTime, engine, entity)
        return false
    }

    override fun onDie(engine: Engine, entity: Entity) {
        createShrapnel(engine, entity)
    }

    private fun createShrapnel(engine: Engine, entity: Entity) {
        val physics = physicsMapper.get(entity)
        val size = sizeMapper.get(entity)
        val body = physics.body ?: return

        v0.set(body.worldCenter)
        for (a in 0 until 360 step (360 / SHRAPNEL_COUNT)) {
            v1.set(-MathUtils.cos(a.toFloat()), MathUtils.sin(a.toFloat())).scl(size.width * .5f * 1.2f).add(v0)
            val shrapnel = Entities.shrapnel(engine, v1.x, v1.y)
            shrapnel.getComponent(Box2DPhysics::class.java)?.apply {
                this.initialAngle = a * MathUtils.degreesToRadians
                this.initialSpeed = 400f
            }
        }
    }
}