package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.spacehorde.components.Dead
import com.spacehorde.components.ParticleOwner
import com.spacehorde.components.component
import com.spacehorde.components.mapper
import com.spacehorde.scripts.Script

class ExplosionScript : Script() {
    private val particleMapper by mapper<ParticleOwner>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {

        val particle = particleMapper.get(entity) ?: return true
        val effect = particle.effect ?: return true
        if (effect.isComplete) {
            entity.add(component<Dead>(engine))
            return true
        }

        return false
    }
}