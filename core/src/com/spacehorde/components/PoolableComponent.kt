package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool

abstract class PoolableComponent : Component, Pool.Poolable {
    override fun reset() {
    }
}