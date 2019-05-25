package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.utils.ObjectMap
import com.spacehorde.components.Tag
import com.spacehorde.components.mapper

class TagSystem : ContainerSystem(Family.all(Tag::class.java).get()) {
    private val entityMap = ObjectMap<String, Entity>()
    private val tagMapper by mapper<Tag>()

    fun getSafe(id: String): Entity? = entityMap[id, null]

    operator fun get(id: String) = entityMap[id, null] ?: throw IllegalArgumentException("Invalid entity tag ID.")

    override fun onEntityAdded(entity: Entity) {
        val tag = tagMapper[entity] ?: return
        if (tag.id == Tag.INVALID) return
        entityMap.put(tag.id, entity)
    }

    override fun onEntityRemoved(entity: Entity) {
        val tag = tagMapper[entity] ?: return
        if (tag.id == Tag.INVALID) return
        entityMap.remove(tag.id)
    }
}