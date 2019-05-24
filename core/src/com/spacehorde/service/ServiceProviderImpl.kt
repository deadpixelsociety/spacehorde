package com.spacehorde.service

abstract class ServiceProviderImpl<T : Any> : ServiceProvider<T> {
    override fun dispose() {
    }
}