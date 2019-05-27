package com.spacehorde.systems

import com.badlogic.ashley.core.*
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.*
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.components.Transform
import com.spacehorde.gdxArray

class Box2DSystem : EntitySystem(), EntityListener, ContactListener {
    companion object {
        const val FRICTION = .985f
    }

    val world = World(Vector2(0f, 0f), false)

    private val transformMapper by mapper<Transform>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val groupMapper by mapper<GroupMask>()
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

        box2d.body?.let {
            it.setTransform(Vector2(body.worldCenter).add(box2d.initialOffset), box2d.initialAngle)
            it.linearVelocity = Vector2(MathUtils.cos(box2d.initialAngle), MathUtils.sin(box2d.initialAngle)).scl(box2d.initialSpeed)
        }
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        val box2d = physicsMapper.get(entity) ?: return
        box2d.body?.let { bodiesToRemove.add(it) }
        box2d.body = null
    }

    override fun beginContact(contact: Contact?) {
        if (contact != null) {
            val entityA = contact.fixtureA?.body?.userData as? Entity
            val entityB = contact.fixtureB?.body?.userData as? Entity

            if (entityA != null && !entityA.has<Dead>()) {
                val box2d = physicsMapper.get(entityA)
                if (box2d != null) {
                    box2d.collision = true
                    box2d.collided = entityB
                    box2d.lastNormal.set(contact.worldManifold.normal)
                }
            }

            if (entityB != null && !entityB.has<Dead>()) {
                val box2d = physicsMapper.get(entityB)
                if (box2d != null) {
                    box2d.collision = true
                    box2d.collided = entityA
                    box2d.lastNormal.set(contact.worldManifold.normal)
                }
            }
        }
    }

    override fun endContact(contact: Contact?) {
        if (contact != null) {
            val entityA = contact.fixtureA?.body?.userData as? Entity
            val entityB = contact.fixtureB?.body?.userData as? Entity

            if (entityA != null) {
                val box2d = physicsMapper.get(entityA)
                if (box2d != null && (box2d.collided == null || box2d.collided == entityB)) {
                    box2d.collision = false
                    box2d.collided = null
                }
            }

            if (entityB != null) {
                val box2d = physicsMapper.get(entityB)
                if (box2d != null && (box2d.collided == null || box2d.collided == entityA)) {
                    box2d.collision = false
                    box2d.collided = null
                }
            }
        }
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }

    override fun update(deltaTime: Float) {
        world.step(deltaTime, 3, 2)

        bodyList.clear()
        world.getBodies(bodyList)

        bodyList.forEach { body ->
            val entity = body.userData as Entity
            val transform = transformMapper.get(entity)
            val group = groupMapper.get(entity)
            val physics = physicsMapper.get(entity) ?: return

            // Don't move spawning entities
            if (physics.manualVelocity && group?.match(Groups.SPAWNING) == false) {
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
        world.setContactListener(this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
        world.setContactListener(null)
        world.dispose()
    }
}
