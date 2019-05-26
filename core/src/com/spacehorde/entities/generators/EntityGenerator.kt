package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity

interface EntityGenerator {
    fun generate(engine: Engine): Entity
}