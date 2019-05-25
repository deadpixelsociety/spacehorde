package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Quaternion
import com.badlogic.gdx.math.Vector2
import com.spacehorde.SpaceHordeGame
import com.spacehorde.components.*
import com.spacehorde.config.CustomControllerMappings
import com.spacehorde.service.service
import de.golfgl.gdx.controllers.mapping.MappedController

class ControlSystem : EntitySystem() {
    companion object {
        private const val DEADZONE = .25f
    }

    private val physicsMapper by mapper<Physics>()
    private val transformMapper by mapper<Transform>()
    private val metaMapper by mapper<Meta>()
    private val weaponizedMapper by mapper<Weaponized>()
    private val mappings by service<CustomControllerMappings>()
    private val v0 = Vector2()
    private val v1 = Vector2()
    private val axis = Vector2()
    private val q0 = Quaternion()
    private val q1 = Quaternion()
    private var tt = 0f

    override fun update(deltaTime: Float) {
        val tagSystem = engine?.getSystem(TagSystem::class.java) ?: return
        val player = tagSystem[Tag.PLAYER]
        val playerPhysics = physicsMapper.get(player) ?: return
        val playerTransform = transformMapper.get(player) ?: return
        val playerMeta = metaMapper.get(player) ?: return
        val playerWeaponized = weaponizedMapper.get(player) ?: return

        val controller = Controllers.getControllers().firstOrNull()
        if (controller != null) {
            val mappedController = MappedController(controller, mappings)

            handleMovement(mappedController, playerTransform, playerPhysics)
            handleFiring(mappedController, player, playerTransform, playerPhysics, playerMeta, playerWeaponized)

            val accept = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_ACCEPT)
            val cancel = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_CANCEL)
            val bomb = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_BOMB)
            val start = mappedController.isButtonPressed(CustomControllerMappings.BUTTON_START)

            if (start) SpaceHordeGame.DEBUG = !SpaceHordeGame.DEBUG
        }

        tt += deltaTime
    }

    private fun handleFiring(mappedController: MappedController, player: Entity, playerTransform: Transform, playerPhysics: Physics, playerMeta: Meta, playerWeaponized: Weaponized) {
        val fx = mappedController.getConfiguredAxisValue(CustomControllerMappings.FIRE_VERTICAL)
        val fy = mappedController.getConfiguredAxisValue(CustomControllerMappings.FIRE_HORIZONTAL)
        axis.set(fy, fx)
        val magnitude = axis.len()
        if (magnitude < DEADZONE) axis.set(0f, 0f)
        else axis.set(axis.nor().scl((magnitude - DEADZONE) / (1 - DEADZONE)))

        if (axis.len() > 0) {
            playerWeaponized.weapons().forEach { weapon ->
                if (weapon.canFire(tt)) {
                    weapon.fire(axis, engine, player)
                    weapon.lastFired = tt
                }
            }
        }
    }

    private fun handleMovement(mappedController: MappedController, transform: Transform, physics: Physics) {
        val mx = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_VERTICAL)
        val my = mappedController.getConfiguredAxisValue(CustomControllerMappings.MOVE_HORIZONTAL)
        axis.set(my, mx)
        val magnitude = axis.len()
        if (magnitude < DEADZONE) axis.set(0f, 0f)
        else axis.set(axis.nor().scl((magnitude - DEADZONE) / (1 - DEADZONE)))

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
}