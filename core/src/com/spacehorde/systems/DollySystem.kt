package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
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

        dolly.camera.reset(false)
        dolly.camera.rotate(transform.angle)
        dolly.camera.position.set(transform.position, 0f)
        dolly.camera.update()
    }
}