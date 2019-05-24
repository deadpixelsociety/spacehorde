package com.spacehorde.service

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ServiceDelegate<T : Any>(private val clazz: Class<T>) : ReadOnlyProperty<Any, T> {
    private lateinit var service: T

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!::service.isInitialized) service = ServiceContainer.get(clazz)
        return service
    }
}