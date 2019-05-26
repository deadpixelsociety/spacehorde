package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import com.spacehorde.Groups
import com.spacehorde.SpaceHordeGame
import com.spacehorde.components.*
import com.spacehorde.config.CustomControllerMappings
import com.spacehorde.service.service
import de.golfgl.gdx.controllers.mapping.MappedController

class ControlSystem : EntitySystem() {
    companion object {
        private const val MOVE_DEADZONE = .33f
        private const val FIRE_DEADZONE = .33f
    }

    private val physicsMapper by mapper<Box2DPhysics>()
    private val transformMapper by mapper<Transform>()
    private val weaponizedMapper by mapper<Weaponized>()
    private val debugMapper by mapper<Debug>()

    private val mappings by service<CustomControllerMappings>()
    private val v0 = Vector2()
    private val axis = Vector2()
    private val q0 = Quaternion()
    private val q1 = Quaternion()
    private var tt = 0f
    private var lastStart = 0f

    override fun update(deltaTime: Float) {
        val groupSystem = engine?.getSystem(GroupSystem::class.java) ?: return
        val players = groupSystem[Groups.PLAYERS]

        val controller = Controllers.getControllers().firstOrNull() ?: return
        players.forEach { player ->
            val mappedController = MappedController(controller, mappings)

            handleMovement(mappedController, player)
            handleFiring(mappedController, player)

            val accept = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_ACCEPT)
            val cancel = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_CANCEL)
            val bomb = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_BOMB)
            val start = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_START)

            if (start && (tt - lastStart >= 1f)) {
                SpaceHordeGame.DEBUG = !SpaceHordeGame.DEBUG
                lastStart = tt
            }
        }

        tt += deltaTime
    }

    private fun handleMovement(mappedController: MappedController, player: Entity) {
        val physics = physicsMapper.get(player) ?: return
        val transform = transformMapper.get(player) ?: return

        val mx = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_VERTICAL)
        val my = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_HORIZONTAL)
        axis.set(my, mx)
        val magnitude = axis.len()
        if (magnitude < MOVE_DEADZONE) axis.set(0f, 0f)
        else axis.set(axis.nor().scl((magnitude - MOVE_DEADZONE) / (1 - MOVE_DEADZONE)))

        if (axis.len() > 0) {
            q0.setFromAxis(0f, 0f, 1f, transform.angle)
            val angle = (MathUtils.atan2(axis.x, axis.y) * MathUtils.radiansToDegrees) + 180f
            q1.setFromAxis(0f, 0f, 1f, angle)
            q0.slerp(q1, physics.rotationSpeed)
            transform.angle = q0.getAngleAround(0f, 0f, 1f)
            physics.acceleration.set(v0.set(0f, 1f).rotate(transform.angle).scl(physics.accelerationSpeed))
        } else {
            physics.acceleration.set(0f, 0f)
        }
    }

    private fun handleFiring(mappedController: MappedController, player: Entity) {
        val weaponized = weaponizedMapper.get(player)
        val debug = debugMapper.get(player)

        val fx = mappedController.getConfiguredAxisValue(CustomControllerMappings.FIRE_VERTICAL)
        val fy = mappedController.getConfiguredAxisValue(CustomControllerMappings.FIRE_HORIZONTAL)
        axis.set(fy, -fx)
        val magnitude = axis.len()
        if (magnitude < FIRE_DEADZONE) axis.set(0f, 0f)
        else axis.set(axis.nor().scl((magnitude - FIRE_DEADZONE) / (1 - FIRE_DEADZONE)))

        debug?.fireAxis?.set(axis)

        if (axis.len() > 0 && weaponized != null) {
            weaponized.weapons().forEach { weapon ->
                if (weapon.canFire(tt)) {
                    weapon.fire(axis, engine, player)
                    weapon.lastFired = tt
                }
            }
        }
    }
}