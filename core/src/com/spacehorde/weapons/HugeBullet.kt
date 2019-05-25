package com.spacehorde.weapons

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.Rotate
import com.spacehorde.ships.ShipColor

class HugeBullet : WeaponDefImpl() {
    companion object {
        private const val BULLET_SIZE = 5f
        private const val BULLET_SPEED = 500f
        private const val BULLET_ACCELERATION = 250f
        private const val BULLET_ROTATION_PER_SECOND = 540f
    }

    private val physicsMapper by mapper<Physics>()
    private val transformMapper by mapper<Transform>()
    private val metaMapper by mapper<Meta>()
    private val bulletTexture by asset<Texture>("textures/bullet.png")
    private val v0 = Vector2()

    override fun fire(axis: Vector2, engine: Engine, owner: Entity) {
        val playerPhysics = physicsMapper.get(owner) ?: return
        val playerTransform = transformMapper.get(owner) ?: return
        val playerMeta = metaMapper.get(owner) ?: return

        v0.set(playerTransform.position).sub(BULLET_SIZE * .5f, BULLET_SIZE * .5f).add(playerTransform.origin)

        val bullet = createBullet(v0.x, v0.y, playerMeta.get<ShipColor>("ShipColor").cockpitColor)
        val transform = transformMapper.get(bullet)
        val physics = physicsMapper.get(bullet)

        val angle = (MathUtils.atan2(axis.x, axis.y) * MathUtils.radiansToDegrees) + 180f
        transform.angle = angle % 360f
        transform.heading.setAngle(transform.angle)
        physics.velocity.set(playerPhysics.velocity)
        physics.acceleration.set(v0.set(0f, 1f).rotate(transform.heading.angle()).scl(physics.accelerationSpeed))
        engine.addEntity(bullet)
    }

    private fun createBullet(x: Float, y: Float, bulletColor: Color): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(BULLET_SIZE * .5f, BULLET_SIZE * .5f)
            scale.set(3f, 3f)
        })

        entity.add(component<Size> {
            width = BULLET_SIZE
            height = BULLET_SIZE
        })

        entity.add(component<Physics> {
            maxSpeed = BULLET_SPEED
            rotationSpeed = 1f / BULLET_ROTATION_PER_SECOND
            accelerationSpeed = BULLET_ACCELERATION
            frictionless = true
        })

        entity.add(component<GroupMask> { mask = GroupMask.BULLETS })
        entity.add(component<Tint> { color.set(bulletColor) })
        entity.add(component<RenderSprite> { sprite = Sprite(bulletTexture) })
        entity.add(component<Scripted> {
            scripts.add(Rotate(BULLET_ROTATION_PER_SECOND))
        })

        return entity
    }
}