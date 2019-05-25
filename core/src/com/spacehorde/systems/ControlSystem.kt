package com.spacehorde.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Physics
import com.spacehorde.components.Transform
import com.spacehorde.config.CustomControllerMappings
import com.spacehorde.entities.Tags
import com.spacehorde.service.service
import de.golfgl.gdx.controllers.mapping.MappedController

class ControlSystem : EntitySystem() {
    companion object {
        private const val DEADZONE = .25f
        private const val TURN_SPEED = .075f
        private const val ACCELERATION = 300f
    }

    private val physicsMapper by lazy { ComponentMapper.getFor(Physics::class.java) }
    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val mappings by service<CustomControllerMappings>()
    private val v0 = Vector2()
    private val movementAxis = Vector2()
    private val fireAxis = Vector2()
    private val q0 = Quaternion()
    private val q1 = Quaternion()

    override fun update(deltaTime: Float) {
        val tagSystem = engine?.getSystem(TagSystem::class.java) ?: return
        val player = tagSystem[Tags.PLAYER] ?: return
        val physics = physicsMapper.get(player) ?: return
        val transform = transformMapper.get(player) ?: return

        val controller = Controllers.getControllers().firstOrNull()
        if (controller != null) {
            val mappedController = MappedController(controller, mappings)
            val vert = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_VERTICAL)
            val horz = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_HORIZONTAL)
            movementAxis.set(horz, vert)
            val magnitude = movementAxis.len()
            if (magnitude < DEADZONE) movementAxis.set(0f, 0f)
            else movementAxis.set(movementAxis.nor().scl((magnitude - DEADZONE) / (1 - DEADZONE)))

            if (movementAxis.len() > 0) {
                q0.setFromAxis(0f, 0f, 1f, transform.rotation)
                val angle = (MathUtils.atan2(movementAxis.x, movementAxis.y) * MathUtils.radiansToDegrees) + 180f
                q1.setFromAxis(0f, 0f, 1f, angle)
                q0.slerp(q1, TURN_SPEED)
                transform.rotation = q0.getAngleAround(0f, 0f, 1f)
                physics.acceleration.set(v0.set(0f, 1f).rotate(transform.rotation).scl(ACCELERATION))
            } else {
                physics.acceleration.set(0f, 0f)
            }

            val accept = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_ACCEPT)
            val cancel = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_CANCEL)
            val bomb = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_BOMB)
            val start = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_START)
        }
    }
}