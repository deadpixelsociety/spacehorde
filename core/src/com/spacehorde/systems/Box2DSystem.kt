package com.spacehorde.systems

import com.badlogic.ashley.core.*
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.World
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.gdxArray

class Box2DSystem : EntitySystem(), EntityListener {
    companion object {
        const val FRICTION = .985f
    }

    val world = World(Vector2(0f, 0f), false)

    private val transformMapper by mapper<Transform>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val bodyList = gdxArray<Body>()
    private val bodiesToRemove = mutableListOf<Body>()
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun entityAdded(entity: Entity?) {
        if (entity == null) return
        val box2d = physicsMapper.get(entity) ?: return
        createBodyAndFixtures(entity, box2d)
    }

    private fun createBodyAndFixtures(entity: Entity, box2d: Box2DPhysics) {
        val body = world.createBody(box2d.bodyDef)
        box2d.body = body
        box2d.fixtureDefs.forEach { body.createFixture(it) }
        body.userData = entity
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        val box2d = physicsMapper.get(entity) ?: return
        box2d.body?.let { bodiesToRemove.add(it) }
        box2d.body = null
    }

    override fun update(deltaTime: Float) {
        world.step(deltaTime, 5, 5)

        bodyList.clear()
        world.getBodies(bodyList)

        bodyList.forEach { body ->
            val entity = body.userData as Entity
            val transform = transformMapper.get(entity)
            val physics = physicsMapper.get(entity)

            if (physics.manualVelocity) {
                v0.set(physics.acceleration).scl(deltaTime)

                v1.set(0f, 0f)
                physics.body?.let {
                    v1.set(it.linearVelocity).add(v0)
                    if (v1.len() > physics.maxSpeed) {
                        v1.nor().scl(physics.maxSpeed)
                    }
                }

                if (!physics.frictionless) v1.scl(FRICTION)
                if (v1.len() < 1f) v1.set(0f, 0f)

                physics.body?.linearVelocity = v1
            }

            transform.position.set(body.position.x - transform.origin.x, body.position.y - transform.origin.y)
            transform.heading.set(0f, 1f).rotate(body.linearVelocity.angle() - 90f).scl(body.linearVelocity.len())
        }

        bodiesToRemove.forEach { body -> world.destroyBody(body) }
        bodiesToRemove.clear()
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.addEntityListener(Family.all(Box2DPhysics::class.java).get(), this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
        world.dispose()
    }
}
