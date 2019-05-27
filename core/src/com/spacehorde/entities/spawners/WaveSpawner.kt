package com.spacehorde.entities.spawners

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.spacehorde.entities.Difficulty
import com.spacehorde.entities.Entities
import com.spacehorde.entities.WaveTracker

typealias SpawnFunc = (Engine) -> Entity

class WaveSpawner(arenaSize: Float) : EnemySpawnerImpl(Vector2(), 50f) {
    companion object {
        private val SPAWNS = mutableMapOf<Difficulty, List<SpawnFunc>>()
        private val SPAWN_LOCATIONS = mutableListOf<Vector2>()
    }

    init {
        val hs = arenaSize * .5f - 60f
        SPAWN_LOCATIONS.add(Vector2(0f, 0f))
        SPAWN_LOCATIONS.add(Vector2(-hs, -hs))
        SPAWN_LOCATIONS.add(Vector2(-hs, 0f))
        SPAWN_LOCATIONS.add(Vector2(-hs, hs))
        SPAWN_LOCATIONS.add(Vector2(hs, -hs))
        SPAWN_LOCATIONS.add(Vector2(hs, 0f))
        SPAWN_LOCATIONS.add(Vector2(hs, hs))
        SPAWN_LOCATIONS.add(Vector2(0f, -hs))
        SPAWN_LOCATIONS.add(Vector2(0f, hs))

        SPAWNS[Difficulty.REDDIT_USER] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyDiamond(engine) }
        )
        SPAWNS[Difficulty.PLEB] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) }
        )
        SPAWNS[Difficulty.NEW_FRIEND] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) }
        )
        SPAWNS[Difficulty.PRIME_PLEB] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
        SPAWNS[Difficulty.SUB] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
        SPAWNS[Difficulty.MOD] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
        SPAWNS[Difficulty.STAFF] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
        SPAWNS[Difficulty.RP_GOD] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
        SPAWNS[Difficulty.GOD_GAMER] = listOf(
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCircle(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemyCross(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemySmall(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyDiamond(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) },
                { engine -> Entities.enemyPinwheel(engine) }
        )
    }

    override fun spawn(engine: Engine, count: Int) {
        position.set(SPAWN_LOCATIONS.random())

        var min = MathUtils.random(10, 20)
        var additional = (MathUtils.random(10, 30) * WaveTracker.difficulty).toInt()

        super.spawn(engine, min + additional)
    }

    override fun createEnemy(engine: Engine): Entity {
        val spawnList = SPAWNS[WaveTracker.difficultyLevel()] ?: throw Exception("fuck")
        return spawnList.random().invoke(engine)
    }
}