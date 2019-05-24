package com.spacehorde.service

import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.ObjectMap

object ServiceContainer : LifecycleListener {
    private val serviceMap = ObjectMap<Class<*>, Any>()

    fun register(service: Any) {
        val clazz = if (service is ServiceProvider<*>) service.providesType else service.javaClass
        register(clazz, service)
    }

    fun register(clazz: Class<*>, service: Any) {
        serviceMap.put(clazz, service)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(clazz: Class<T>): T {
        if (!serviceMap.containsKey(clazz)) throw Exception("Missing service provider for class ${clazz.name}")
        val service = serviceMap.get(clazz)
        return service as T
    }

    inline fun <reified T : Any> get(): T {
        return get(T::class.java)
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        serviceMap.values().filterIsInstance(Disposable::class.java).forEach { it.dispose() }
    }
}