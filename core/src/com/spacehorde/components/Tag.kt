package com.spacehorde.components

class Tag : PoolableComponent() {
    companion object {
        const val INVALID = ""
        const val PLAYER = "PLAYER"
    }

    var id = INVALID

    override fun reset() {
        id = INVALID
    }
}
