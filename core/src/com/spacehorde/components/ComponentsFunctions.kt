package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pools

inline fun <reified T : Component> mapper() = ComponentMapperDelegate(T::class.java)

inline fun <reified T : Component> component(init: T.(T) -> Unit): T {
    val component = Pools.obtain(T::class.java)
    component.init(component)
    return component
}

inline fun <reified T : Component> component(): T = component { }
