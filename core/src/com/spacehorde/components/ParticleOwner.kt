package com.spacehorde.components

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.g2d.ParticleEffect

class ParticleOwner : PoolableComponent() {
    var effect: ParticleEffect? = null
    var update: ((Engine, Entity, ParticleEffect) -> Unit) = { _, _, _ -> }

    override fun reset() {
        effect = null
        update = { _, _, _ -> }
    }
}