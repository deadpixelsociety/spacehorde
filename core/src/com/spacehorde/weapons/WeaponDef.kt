package com.spacehorde.weapons

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

interface WeaponDef {
    val duration: Float

    val fireDelay: Float

    var lastFired: Float

    fun canFire(currentTime: Float) = (currentTime - lastFired) >= fireDelay

    fun fire(axis: Vector2, engine: Engine, owner: Entity)
}