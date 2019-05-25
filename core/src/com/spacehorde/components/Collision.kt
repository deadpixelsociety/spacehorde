package com.spacehorde.components

import com.badlogic.ashley.core.Entity

class Collision : PoolableComponent() {
    var collision = false
    var collided: Entity? = null
    var mask = GroupMask.INVALID

    override fun reset() {
        collision = false
        collided = null
        mask = 0
    }
}