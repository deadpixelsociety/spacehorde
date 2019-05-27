package com.spacehorde.scripts.impl

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.scripts.Script

class SpawnScript : Script() {
    private val groupMapper by mapper<GroupMask>()
    private val physicsMapper by mapper<Box2DPhysics>()
    private val scriptMapper by mapper<Scripted>()
    private val tintMapper by mapper<Tint>()

    var spawnTime = 2f
    private var originalGroup = Groups.NONE
    private var originalCategory = Groups.NONE
    private val fadeScript = FadeScript(.25f, .5f)

    override fun start(engine: Engine, entity: Entity) {
        super.start(engine, entity)
        val group = groupMapper.get(entity)
        val physics = physicsMapper.get(entity)
        val scripted = scriptMapper.get(entity)

        originalGroup = group?.mask ?: Groups.NONE
        originalCategory = physics?.body?.fixtureList?.firstOrNull()?.filterData?.categoryBits ?: Groups.NONE

        group?.mask = Groups.SPAWNING
        val filter = physics?.body?.fixtureList?.firstOrNull()?.filterData
        filter?.let { it.categoryBits = Groups.SPAWNING }
        physics?.body?.fixtureList?.firstOrNull()?.filterData = filter

        scripted.scripts.add(fadeScript)
    }

    override fun finish(engine: Engine, entity: Entity) {
        super.finish(engine, entity)
        val group = groupMapper.get(entity)
        val physics = physicsMapper.get(entity)
        val tint = tintMapper.get(entity)

        fadeScript.finished = true

        tint.color.a = 1f

        group?.mask = originalGroup
        val filter = physics?.body?.fixtureList?.firstOrNull()?.filterData
        filter?.let { it.categoryBits = originalCategory }
        physics?.body?.fixtureList?.firstOrNull()?.filterData = filter
    }

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (spawnTime > 0f) spawnTime -= deltaTime
        return spawnTime <= 0f
    }
}