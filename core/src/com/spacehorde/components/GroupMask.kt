package com.spacehorde.components

class GroupMask : PoolableComponent() {
    companion object {
        const val INVALID = 0
        const val ENEMIES = 1
        const val BULLETS = 2
        const val WALLS = 4
    }

    var mask = INVALID

    override fun reset() {
        mask = INVALID
    }
}