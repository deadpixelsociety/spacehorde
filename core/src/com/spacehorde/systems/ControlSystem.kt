package com.spacehorde.systems

import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.Physics
import com.spacehorde.components.Transform
import com.spacehorde.entities.Entities
import com.spacehorde.entities.Tags

class ControlSystem : EntitySystem() {
    private val physicsMapper by lazy { ComponentMapper.getFor(Physics::class.java) }
    private val transformMapper by lazy { ComponentMapper.getFor(Transform::class.java) }
    private val v0 = Vector2()

    override fun update(deltaTime: Float) {
        val tagSystem = engine?.getSystem(TagSystem::class.java) ?: return
        val player = tagSystem[Tags.PLAYER] ?: return
        val physics = physicsMapper.get(player) ?: return
        val transform = transformMapper.get(player) ?: return

        // TODO: Control re-binding
        // TODO: Generalize control system (consideration towards networking)

        var force = 0f
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            force += 100f
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            force -= 100f
        }

        if (force != 0f) {
            physics.acceleration.set(v0.set(transform.heading).scl(force))
        } else {
            physics.acceleration.set(0f, 0f)
        }

        // TODO: Instead of instant rotation, maybe apply some sort of accelerated rotation applied in physics?
        var rotation = transform.rotation
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotation -= 3f
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotation += 3f
        }

        transform.rotation = rotation
    }
}