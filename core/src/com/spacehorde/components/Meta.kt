package com.spacehorde.components

import com.badlogic.gdx.utils.ObjectMap

class Meta : PoolableComponent() {
    private val data = ObjectMap<String, Any>()

    fun put(name: String, info: Any) {
        data.put(name, info)
    }

    fun <T : Any> get(name: String): T = data.get(name) as T

    override fun reset() {
        data.clear()
    }
}