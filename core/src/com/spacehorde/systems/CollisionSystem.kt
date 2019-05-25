package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Rectangle
import com.spacehorde.components.*

class CollisionSystem : IteratingSystem(Family.all(Transform::class.java, Size::class.java, Collision::class.java).get()) {
    private val transformMapper by mapper<Transform>()
    private val sizeMapper by mapper<Size>()
    private val groupMapper by mapper<GroupMask>()
    private val collisionMapper by mapper<Collision>()
    private val b0 = Rectangle()
    private val b1 = Rectangle()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return
        val transform = transformMapper.get(entity) ?: return
        val size = sizeMapper.get(entity) ?: return
        val collision = collisionMapper.get(entity) ?: return
        val spatialSystem = engine.getSystem(SpatialSystem::class.java) ?: return

        b0.setCenter(transform.position.x + transform.origin.x, transform.position.y + transform.origin.y)
        b0.setSize(size.width, size.height)

        var collided = false

        val neighbors = spatialSystem.getNeighbors(entity)
        neighbors.forEach {
            val neighborTransform = transformMapper.get(it)
            val neighborSize = sizeMapper.get(it)
            val neighborCollision = collisionMapper.get(it)
            val neighborGroup = groupMapper.get(it)

            if (neighborTransform != null && neighborSize != null && neighborCollision != null && neighborGroup != null) {
                if (collision.mask.and(neighborGroup.mask) == neighborGroup.mask) {
                    val nx = neighborTransform.position.x + neighborTransform.origin.x
                    val ny = neighborTransform.position.y + neighborTransform.origin.y
                    b1.setCenter(nx, ny)
                    b1.setSize(neighborSize.width, neighborSize.height)

                    if (b0.contains(b1) || b0.overlaps(b1)) {
                        collided = true
                        collision.collision = true
                        collision.collided = it
                        neighborCollision.collision = true
                        neighborCollision.collided = entity
                    }
                }
            }
        }

        if (!collided) {
            collision.collision = false
            collision.collided = null
        }
    }
}