package com.spacehorde.entities.spawners

import com.badlogic.ashley.core.Engine
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Box2DPhysics
import com.spacehorde.components.Scripted
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.scripts.impl.SpawnScript

abstract class EnemySpawnerImpl(override val position: Vector2, override val radius: Float = 50f) : EnemySpawner {
    private val scriptMapper by mapper<Scripted>()
    private val v0 = Vector2()

    override fun spawn(engine: Engine, count: Int) {
        for (i in 0 until count) {
            val entity = createEnemy(engine)

            v0.set(0f, 1f).rotate(MathUtils.random(360f)).scl(MathUtils.random(radius)).add(position)

            entity.getComponent(Transform::class.java)?.apply {
                this.position.set(v0)
            }

            entity.getComponent(Box2DPhysics::class.java)?.apply {
                this.bodyDef.position.set(v0)
                this.body?.setTransform(v0, 0f)
            }

            val scripted = scriptMapper.get(entity)
            scripted?.scripts?.add(SpawnScript())
        }
    }
}