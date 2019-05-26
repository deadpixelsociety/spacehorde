package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.ObjectMap
import com.spacehorde.Groups
import com.spacehorde.components.GroupMask
import com.spacehorde.components.mapper
import com.spacehorde.gdxArray
import kotlin.experimental.and

class GroupSystem : ContainerSystem(Family.all(GroupMask::class.java).get()) {
    private val entityMap = ObjectMap<Short, Array<Entity>>()
    private val groupMapper by mapper<GroupMask>()

    operator fun get(mask: Short): List<Entity> {
        val list = arrayListOf<Entity>()
        entityMap.forEach {
            val id = it.key.toShort()
            if ((mask and id) == id) list.addAll(getInner(id))
        }

        return list
    }

    private fun getInner(id: Short): Array<Entity> {
        var array = entityMap.get(id)
        if (array == null) {
            array = gdxArray()
            entityMap.put(id, array)
        }

        return array
    }

    override fun onEntityAdded(entity: Entity) {
        val group = groupMapper[entity] ?: return
        if (group.mask == Groups.NONE) return
        for (i in 0..15) {
            val id = (1 shl i).toShort()
            if ((group.mask and id) == id) getInner(id).add(entity)
        }
    }

    override fun onEntityRemoved(entity: Entity) {
        val group = groupMapper[entity] ?: return
        if (group.mask == Groups.NONE) return
        for (i in 0..15) {
            val id = (1 shl i).toShort()
            if ((group.mask and id) == id) getInner(id).removeValue(entity, true)
        }
    }
}