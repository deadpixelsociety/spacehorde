package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.spacehorde.components.Size
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.service.service

class DebugRenderSystem(private val camera: Camera)
    : IteratingSystem(Family.all(Transform::class.java).get()) {

    companion object {
        private const val POSITION = true
        private const val ORIGIN = true
        private const val HEADING = true
        private const val BOUNDS = false
    }

    private val transformMapper by mapper<Transform>()
    private val sizeMapper by mapper<Size>()
    private val shape by service<ShapeRenderer>()
    private val box2DDebugRenderer = Box2DDebugRenderer()
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun checkProcessing() = false

    override fun update(deltaTime: Float) {
        begin()
        //renderSpatial()
        super.update(deltaTime)
        end()

        val box2DSystem = engine.getSystem(Box2DSystem::class.java)
        box2DDebugRenderer.render(box2DSystem.world, camera.combined)
    }

    private fun renderSpatial() {
        val spatialSystem = engine.getSystem(SpatialSystem::class.java)
        shape.color = Color.BLUE

        val size = SpatialSystem.CELL_SIZE
        for (y in 0 until spatialSystem.rows) {
            for (x in 0 until spatialSystem.cols) {
                shape.box(x * size, y * size, 0f, size, size, 0f)
            }
        }
    }

    private fun begin() {
        shape.projectionMatrix = camera.combined
        shape.begin(ShapeRenderer.ShapeType.Line)
    }

    private fun end() {
        shape.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return

        val transform = transformMapper.get(entity)
        val size = sizeMapper.get(entity)
        //val spatialSystem = engine.getSystem(SpatialSystem::class.java)

        if (POSITION) {
            shape.color = Color.BLUE
            shape.circle(transform.position.x, transform.position.y, 2f)
        }

        if (ORIGIN) {
            v0.set(transform.position).add(transform.origin)
            shape.color = Color.CYAN
            shape.circle(v0.x, v0.y, 2f)
        }

        if (HEADING) {
            v1.set(transform.heading).scl(.1f)
            shape.color = Color.RED
            shape.line(v0.x, v0.y, v0.x + v1.x, v0.y + v1.y)
        }

        if (BOUNDS) {
            shape.color = Color.CORAL
            val sx = size.width * transform.scale.x * .5f
            val sy = size.height * transform.scale.y * .5f
            shape.box(v0.x - sx, v0.y - sy, 0f, sx * 2f, sy * 2f, 0f)
        }

        /*
        shape.color = Color.YELLOW
        val neighbors = spatialSystem.getNeighbors(entity)
        neighbors.forEach {
            val neighborTransform = transformMapper.get(it)
            shape.line(transform.position, neighborTransform.position)
        }
        */

        shape.color = Color.WHITE
    }
}