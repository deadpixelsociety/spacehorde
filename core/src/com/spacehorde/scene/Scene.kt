package com.spacehorde.scene

import com.badlogic.gdx.utils.Disposable

interface Scene : Disposable {
    val overlay: Boolean

    fun create()

    fun resize(width: Int, height: Int)

    fun pause()

    fun resume()

    fun draw()

    fun update(dt: Float, tt: Float)
}