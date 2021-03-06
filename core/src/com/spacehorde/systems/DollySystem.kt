package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Dolly
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.reset

class DollySystem : IteratingSystem(Family.all(Transform::class.java, Dolly::class.java).get()) {
    private val transformMapper by mapper<Transform>()
    private val dollyMapper by mapper<Dolly>()

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return
        val transform = transformMapper[entity] ?: return
        val dolly = dollyMapper[entity] ?: return

        val camera = dolly.camera
        if (camera != null) {
            camera.reset(false)
            camera.rotate(transform.angle)
            camera.position.set(transform.position, 0f)
            camera.update()
        }
    }
}