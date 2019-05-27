package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.spacehorde.components.Dying
import com.spacehorde.components.component
import com.spacehorde.scripts.Script

class KillScript : Script() {
    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {

        entity.add(component<Dying>(engine))
        return true
    }
}