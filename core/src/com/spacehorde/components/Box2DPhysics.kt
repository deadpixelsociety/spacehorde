package com.spacehorde.components

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef

class Box2DPhysics : PoolableComponent() {
    var bodyDef = BodyDef()
    var body: Body? = null
    val fixtureDefs = mutableListOf<FixtureDef>()

    var initialSpeed = 0f
    var initialAngle = 0f
    val initialOffset = Vector2()

    var collision = false
    var collided: Entity? = null
    val lastNormal = Vector2()

    val acceleration = Vector2()
    var rotationSpeed = 1f
    var accelerationSpeed = 100f
    var maxSpeed = 100f

    var frictionless = false
    var manualVelocity = false

    override fun reset() {
        bodyDef = BodyDef()
        body = null
        fixtureDefs.clear()
        initialAngle = 0f
        initialSpeed = 0f
        initialOffset.set(0f, 0f)
        collision = false
        collided = null
        lastNormal.set(0f, 0f)
        acceleration.set(0f, 0f)
        rotationSpeed = 1f
        maxSpeed = 100f
        accelerationSpeed = 100f
        frictionless = false
        manualVelocity = false
    }
}