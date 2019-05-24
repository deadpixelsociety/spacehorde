package com.spacehorde.service

abstract class InstanceServiceProvider<T : Any> : ServiceProvider<T> {
    override fun provide(): T {
        return provideInstance()
    }

    abstract fun provideInstance(): T

    override fun dispose() {
    }
}