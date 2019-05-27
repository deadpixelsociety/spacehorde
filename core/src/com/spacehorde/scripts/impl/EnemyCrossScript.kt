package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

class EnemyCrossScript : EnemyScript() {
    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        super.update(deltaTime, engine, entity)
        evadeAndChasePlayer(engine, entity)
        return false
    }
}