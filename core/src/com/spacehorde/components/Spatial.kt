package com.spacehorde.components

class Spatial : PoolableComponent() {
    val cells = mutableListOf<Pair<Int, Int>>()

    override fun reset() {
        cells.clear()
    }
}