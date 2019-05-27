package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.spacehorde.components.*
import com.spacehorde.entities.Entities
import com.spacehorde.scripts.Script
import com.spacehorde.ships.ShipColor

class PlayerScript : Script() {
    private val metaMapper by mapper<Meta>()
    private val transformMapper by mapper<Transform>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (entity.has<Dying>()) {
            die(engine, entity)
        }

        return false
    }

    private fun die(engine: Engine, entity: Entity) {

        if (!entity.has<Dead>()) {
            entity.add(component<Dead>(engine))
            val transform = transformMapper.get(entity)
            val meta = metaMapper.get(entity)

            var color = Color.WHITE
            if (meta != null) color = meta.get<ShipColor>("ShipColor").cockpitColor

            Entities.explosion(engine, transform.position.x + transform.origin.x, transform.position.y + transform.origin.y, color)
        }
    }
}