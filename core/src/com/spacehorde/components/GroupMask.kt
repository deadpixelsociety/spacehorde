package com.spacehorde.components

class GroupMask : PoolableComponent() {
    companion object {
        const val INVALID = 0
        const val ENEMIES = 1
        const val BULLETS = 2
        const val WALLS = 4
        const val PLAYERS = 8
    }

    var mask = INVALID

    fun has(id: Int) = mask.and(id) == id

    override fun reset() {
        mask = INVALID
    }
}