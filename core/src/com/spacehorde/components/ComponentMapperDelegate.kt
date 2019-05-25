package com.spacehorde.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.ComponentMapper
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ComponentMapperDelegate<T : Component>(componentClass: Class<T>) : ReadOnlyProperty<Any, ComponentMapper<T>> {
    private val mapper = ComponentMapper.getFor(componentClass)

    override fun getValue(thisRef: Any, property: KProperty<*>): ComponentMapper<T> = mapper
}