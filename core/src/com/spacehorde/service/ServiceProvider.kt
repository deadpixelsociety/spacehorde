package com.spacehorde.service

import com.badlogic.gdx.utils.Disposable

interface ServiceProvider<T : Any> : Disposable {
    val providesType: Class<T>

    fun provide(): T
}