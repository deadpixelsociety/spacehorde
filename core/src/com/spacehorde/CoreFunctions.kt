package com.spacehorde

import com.badlogic.gdx.utils.Disposable

fun <T : Disposable, R> T.using(func: (T) -> R): R {
    try {
        return func(this)
    } finally {
        dispose()
    }
}