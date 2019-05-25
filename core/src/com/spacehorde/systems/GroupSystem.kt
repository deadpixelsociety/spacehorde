package com.spacehorde.systems

import com.badlogic.ashley.core.*
import com.badlogic.gdx.utils.ObjectMap
import com.spacehorde.components.Group

class GroupSystem : EntitySystem(), EntityListener {
    private val groupMapper by lazy { ComponentMapper.getFor(Group::class.java) }
    private val groupMap = ObjectMap<String, com.badlogic.gdx.utils.Array<Entity>>()

    fun get(name: String): List<Entity> = getInner(name).toList()

    private fun getInner(name: String): com.badlogic.gdx.utils.Array<Entity> {
        var entities = groupMap.get(name, null)
        if (entities == null) {
            entities = com.badlogic.gdx.utils.Array()
            groupMap.put(name, entities)
        }

        return entities
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.addEntityListener(Family.all(Group::class.java).get(), this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        val group = groupMapper.get(entity) ?: return
        getInner(group.name).removeValue(entity, true)
    }

    override fun entityAdded(entity: Entity?) {
        if (entity == null) return
        val group = groupMapper.get(entity) ?: return
        getInner(group.name).add(entity)
    }
}