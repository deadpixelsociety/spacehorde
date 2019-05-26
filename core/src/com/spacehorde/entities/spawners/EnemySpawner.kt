package com.spacehorde.entities.spawners

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2

interface EnemySpawner {
    val position: Vector2
    val radius: Float

    fun spawn(engine: Engine, count: Int)
    fun createEnemy(engine: Engine): Entity
}