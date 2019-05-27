package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.utils.Pools
import com.spacehorde.components.*
import com.spacehorde.graphics.Fonts

class ScoreTextGenerator : EntityGenerator {
    override fun generate(engine: Engine): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Transform>(engine))
        entity.add(component<Text>(engine) { font = Fonts.scoreText })
        entity.add(component<Size>(engine))
        entity.add(component<Tint>(engine))
        entity.add(component<Scripted>(engine))

        return entity
    }
}