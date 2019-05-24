package com.spacehorde.service

import com.badlogic.gdx.utils.Disposable

abstract class SingletonServiceProvider<T : Any> : ServiceProvider<T> {
    private lateinit var instance: T

    override fun provide(): T {
        if (!::instance.isInitialized) instance = provideSingleton()
        return instance
    }

    abstract fun provideSingleton(): T

    override fun dispose() {
        if (::instance.isInitialized) {
            val disposable = instance as? Disposable
            disposable?.dispose()
        }
    }
}