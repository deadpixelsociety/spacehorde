package com.spacehorde.components

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body

class Box2D : PoolableComponent() {
    var body: Body? = null
    var collision = false
    var collided: Entity? = null
    val lastNormal = Vector2()

    override fun reset() {
        body = null
        collision = false
        collided = null
        lastNormal.set(0f, 0f)
    }
}