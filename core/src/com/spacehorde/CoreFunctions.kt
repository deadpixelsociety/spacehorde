package com.spacehorde

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Disposable

typealias Action<T> = (T) -> Unit
typealias Func<T> = () -> T
typealias Predicate<T> = (T) -> Boolean
typealias Transform<T, R> = (T) -> R
typealias Tween<T> = (T, T, Float) -> T

fun <T> gdxArray() = com.badlogic.gdx.utils.Array<T>()

fun <T : Disposable> T.using(action: Action<T>): T {
    try {
        action(this)
        return this
    } finally {
        dispose()
    }
}

fun <T : Disposable, R : Any> T.usingWith(transform: Transform<T, R>): R {
    try {
        return transform(this)
    } finally {
        dispose()
    }
}

fun Vector2.toVector3(z: Float = 0f) = Vector3(x, y, z)

fun Vector3.toVector2() = Vector2(x, y)

fun Vector2.withLargestComponent(): Vector2 {
    val xl = Math.abs(x) >= Math.abs(y)
    return Vector2(if (xl) x else 0f, if (xl) 0f else y)
}

fun <T : OrthographicCamera> T.reset(yDown: Boolean) {
    if (yDown) {
        up.set(0f, -1f, 0f)
        direction.set(0f, 0f, 1f)
    } else {
        up.set(0f, 1f, 0f)
        direction.set(0f, 0f, -1f)
    }
}