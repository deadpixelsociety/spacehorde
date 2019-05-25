package com.spacehorde.systems

import com.badlogic.ashley.core.*
import com.badlogic.gdx.utils.ObjectMap
import com.spacehorde.components.Tag

class TagSystem : EntitySystem(), EntityListener {
    private val tagMapper by lazy { ComponentMapper.getFor(Tag::class.java) }
    private val tagMap = ObjectMap<String, Entity>()

    operator fun get(name: String): Entity? = tagMap.get(name, null)

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.addEntityListener(Family.all(Tag::class.java).get(), this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        val tag = tagMapper.get(entity) ?: return
        tagMap.remove(tag.name)
    }

    override fun entityAdded(entity: Entity?) {
        if (entity == null) return
        val tag = tagMapper.get(entity) ?: return
        tagMap.put(tag.name, entity)
    }
}