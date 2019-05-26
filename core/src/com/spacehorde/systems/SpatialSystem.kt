package com.spacehorde.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntityListener
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Size
import com.spacehorde.components.Spatial
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper

class SpatialSystem(val width: Float, val height: Float)
    : IteratingSystem(Family.all(Transform::class.java, Size::class.java, Spatial::class.java).get()), EntityListener {

    companion object {
        const val CELL_SIZE = 75f
        private val NEIGHBORS = listOf(
                Vector2(0f, 0f),
                Vector2(-1f, 0f),
                Vector2(-1f, 1f),
                Vector2(0f, 1f),
                Vector2(1f, 1f),
                Vector2(1f, 0f),
                Vector2(1f, -1f),
                Vector2(0f, -1f),
                Vector2(-1f, -1f)
        )
    }

    val cols = MathUtils.ceil(width / CELL_SIZE)
    val rows = MathUtils.ceil(height / CELL_SIZE)

    private val transformMapper by mapper<Transform>()
    private val sizeMapper by mapper<Size>()
    private val spatialMapper by mapper<Spatial>()
    private val cells = Array(rows) { Array(cols) { mutableListOf<Entity>() } }
    private val v0 = Vector2()
    private val r0 = Rectangle()
    private val r1 = Rectangle()

    override fun entityAdded(entity: Entity?) {
        placeEntity(entity)
    }

    override fun entityRemoved(entity: Entity?) {
        if (entity == null) return
        removeEntity(entity)
    }

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        engine?.addEntityListener(family, this)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        engine?.removeEntityListener(this)
    }

    override fun update(deltaTime: Float) {
        clearCells()
        super.update(deltaTime)
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        placeEntity(entity)
    }

    fun getNeighbors(entity: Entity): List<Entity> {
        val list = mutableListOf<Entity>()
        val transform = transformMapper.get(entity) ?: return list
        val x = MathUtils.floor((transform.position.x + transform.origin.x) / CELL_SIZE)
        val y = MathUtils.floor((transform.position.y + transform.origin.y) / CELL_SIZE)

        NEIGHBORS.forEach {
            val nx = (x + it.x).toInt()
            val ny = (y + it.y).toInt()
            if (withinBounds(nx, ny)) list.addAll(cells[ny][nx])
        }

        list.remove(entity)

        return list
    }

    private fun withinBounds(x: Int, y: Int) = x in 0 until cols && y in 0 until rows

    private fun clearCells() {
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                cells[y][x].clear()
            }
        }
    }

    private fun placeEntity(entity: Entity?) {
        if (entity == null) return
        val transform = transformMapper.get(entity) ?: return
        val size = sizeMapper.get(entity) ?: return
        val spatial = spatialMapper.get(entity) ?: return
        val bounds = getBounds(transform, size)

        spatial.cells.clear()

        for (y in 0 until rows) {
            for (x in 0 until cols) {
                r1.set(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE)
                if (r1.contains(bounds) || r1.overlaps(bounds)) {
                    cells[y][x].add(entity)
                    spatial.cells.add(Pair(x, y))
                }
            }
        }
    }

    private fun removeEntity(entity: Entity) {
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                cells[y][x].remove(entity)
            }
        }
    }

    private fun getBounds(transform: Transform, size: Size): Rectangle {
        r0.setCenter(v0.set(transform.position).add(transform.origin))
        r0.setSize(size.width, size.height)
        return r0
    }
}
