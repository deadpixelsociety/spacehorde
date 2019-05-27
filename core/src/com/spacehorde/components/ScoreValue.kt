package com.spacehorde.components

class ScoreValue : PoolableComponent() {
    var value = 0

    override fun reset() {
        value = 0
    }
}