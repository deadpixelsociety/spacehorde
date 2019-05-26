package com.spacehorde.entities.spawners

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.spacehorde.entities.Entities

class EnemySmallSpawner(override val position: Vector2, override val radius: Float = 100f)
    : EnemySpawnerImpl(position, radius) {

    override fun createEnemy(engine: Engine): Entity {
        return Entities.enemySmall(engine)
    }
}