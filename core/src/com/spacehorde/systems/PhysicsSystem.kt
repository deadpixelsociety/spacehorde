package com.spacehorde.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Physics
import com.spacehorde.components.Transform

class PhysicsSystem : IteratingSystem(Family.all(Transform::class.java, Physics::class.java).get()), EntityListener {
    companion object {
        const val FRICTION = .985f
    }

    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val physicsMapper by lazy { ComponentMapper.getFor(Physics::class.java) }
    private val v0 = Vector2()

    override fun entityRemoved(entity: Entity?) {
    }

    override fun entityAdded(entity: Entity?) {
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return

        val transform = transformMapper.get(entity) ?: return
        val physics = physicsMapper.get(entity) ?: return

        integrate(deltaTime, physics, transform)
        updateHeading(transform)
    }

    private fun updateHeading(transform: Transform) {
        transform.heading.set(v0.set(0f, 1f).rotate(transform.rotation))
    }

    private fun integrate(deltaTime: Float, physics: Physics, transform: Transform) {
        v0.set(physics.acceleration).scl(deltaTime)
        physics.velocity.add(v0)

        if (physics.velocity.len() > physics.maxSpeed) {
            physics.velocity.nor().scl(physics.maxSpeed)
        }

        physics.velocity.scl(FRICTION)
        v0.set(physics.velocity).scl(deltaTime)
        transform.position.add(v0)
    }
}