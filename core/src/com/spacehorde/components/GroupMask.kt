package com.spacehorde.components

import com.spacehorde.Groups
import kotlin.experimental.and

class GroupMask : PoolableComponent() {
    var mask: Short = Groups.NONE

    fun match(id: Short) = mask.and(id) == id

    override fun reset() {
        mask = Groups.NONE
    }
}