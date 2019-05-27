package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pools

inline fun <reified T : Component> mapper() = ComponentMapperDelegate(T::class.java)

inline fun <reified T : Component> component(engine: Engine, init: T.(T) -> Unit): T {
    val component = Pools.obtain(T::class.java)
    component.init(component)
    return component
}

inline fun <reified T : Component> component(engine: Engine): T = component(engine) { }

inline fun <reified T : Component> Entity.has() = getComponent(T::class.java) != null