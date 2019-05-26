package com.spacehorde.weapons

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.Rotate
import com.spacehorde.ships.ShipColor
import kotlin.experimental.or

class TwinBullet : WeaponDefImpl() {
    companion object {
        private const val BULLET_SIZE = 5f
        private const val BULLET_SPEED = 300f
        private const val BULLET_ACCELERATION = 750f
        private const val BULLET_ROTATION_PER_SECOND = 540f
    }

    private val physicsMapper by mapper<Box2DPhysics>()
    private val transformMapper by mapper<Transform>()
    private val metaMapper by mapper<Meta>()
    private val sizeMapper by mapper<Size>()
    private val bulletTexture by asset<Texture>("textures/bullet.png")
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun fire(axis: Vector2, engine: Engine, owner: Entity) {
        val physics = physicsMapper.get(owner) ?: return
        val transform = transformMapper.get(owner) ?: return
        val meta = metaMapper.get(owner) ?: return
        val size = sizeMapper.get(owner) ?: return

        val color = meta.get<ShipColor>("ShipColor").cockpitColor
        val velocity = physics.body?.linearVelocity ?: return

        v0.set(transform.position).sub(BULLET_SIZE * .5f, BULLET_SIZE * .5f).add(transform.origin)
        engine.addEntity(addBullet(v0, axis, -size.width * .5f, velocity, color))
        v0.set(transform.position).sub(BULLET_SIZE * .5f, BULLET_SIZE * .5f).add(transform.origin)
        engine.addEntity(addBullet(v0, axis, size.width * .5f, velocity, color))
    }

    private fun addBullet(position: Vector2, axis: Vector2, offset: Float, initialVelocity: Vector2, color: Color): Entity {
        val bullet = createBullet(position.x, position.y, color)
        val transform = transformMapper.get(bullet)
        val physics = physicsMapper.get(bullet)

        val angle = (MathUtils.atan2(axis.x, axis.y) * MathUtils.radiansToDegrees) + 180f
        transform.angle = angle % 360f
        transform.heading.set(0f, 1f).rotate(transform.angle)

        v1.set(offset, 0f).rotate(transform.angle).add(position)
        transform.position.set(v1)
        physics.body?.position?.set(v1)

        v1.set(0f, 1f).rotate(transform.heading.angle()).scl(physics.accelerationSpeed).add(initialVelocity)

        physics.body?.linearVelocity = v1
        //physics.acceleration.set(v1.set(0f, 1f).rotate(transform.heading.angle()).scl(physics.accelerationSpeed))
        return bullet
    }

    private fun createBullet(x: Float, y: Float, bulletColor: Color): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(BULLET_SIZE * .5f, BULLET_SIZE * .5f)
        })

        entity.add(component<Size> {
            width = BULLET_SIZE
            height = BULLET_SIZE
        })

        entity.add(component<Box2DPhysics> {
            bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.DynamicBody
                this.bullet = true
                this.fixedRotation = true
                this.position.set(x, y)
            }

            fixtureDefs.add(FixtureDef().apply {
                this.filter.categoryBits = Groups.BULLETS
                this.filter.maskBits = Groups.ENEMIES.or(Groups.WALLS)

                this.restitution = .9f
                this.shape = CircleShape().apply {
                    this.radius = BULLET_SIZE * .5f * 1.1f
                }
            })

            maxSpeed = BULLET_SPEED
            rotationSpeed = 1f / BULLET_ROTATION_PER_SECOND
            accelerationSpeed = BULLET_ACCELERATION
            frictionless = true
        })

        entity.add(component<GroupMask> { mask = Groups.BULLETS })
        entity.add(component<Tint> { color.set(bulletColor) })
        entity.add(component<RenderSprite> { sprite = Sprite(bulletTexture) })
        entity.add(component<Scripted> {
            scripts.add(Rotate(BULLET_ROTATION_PER_SECOND))
        })

        return entity
    }
}