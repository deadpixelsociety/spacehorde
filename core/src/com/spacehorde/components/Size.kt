package com.spacehorde.components

class Size : PoolableComponent() {
    var width = 0f

    var height = 0f

    var radius = 0f

    override fun reset() {
        width = 0f
        height = 0f
        radius = 0f
    }
}