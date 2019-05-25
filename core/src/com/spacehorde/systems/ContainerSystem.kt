package com.spacehorde.systems

import com.badlogic.ashley.core.*

open class ContainerSystem(val family: Family, priority: Int) : EntitySystem(priority), EntityListener {
    private val entities = hashSetOf<Entity>()

    constructor(family: Family) : this(family, 0)

    fun entities() = entities.toList()

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
    }

    override fun entityAdded(entity: Entity?) {
        if (entity == null) return
        if (entities.add(entity)) onEntityAdded(entity)
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        if (entities.remove(entity)) onEntityRemoved(entity)
    }

    protected open fun onEntityAdded(entity: Entity) {
    }

    protected open fun onEntityRemoved(entity: Entity) {
    }
}