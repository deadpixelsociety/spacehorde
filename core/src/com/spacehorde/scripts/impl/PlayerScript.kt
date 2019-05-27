package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.components.Dead
import com.spacehorde.components.Dying
import com.spacehorde.components.component
import com.spacehorde.components.has
import com.spacehorde.scripts.Script

class PlayerScript : Script() {
    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (entity.has<Dying>()) {
            die(entity)
        }

        return false
    }

    private fun die(entity: Entity) {
        if (!entity.has<Dead>()) {
            entity.add(component<Dead>())
            // TODO: particles, sounds!
        }
    }
}