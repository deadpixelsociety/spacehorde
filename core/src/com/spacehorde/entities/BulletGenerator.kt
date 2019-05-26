package com.spacehorde.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.Rotate
import com.spacehorde.scripts.impl.BulletScript
import kotlin.experimental.or

class BulletGenerator : EntityGenerator {
    companion object {
        private const val BULLET_ROTATION_PER_SECOND = 540f
    }

    private val bulletTexture by asset<Texture>("textures/bullet.png")

    override fun generate(engine: Engine): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Transform>())
        entity.add(component<Size>())

        entity.add(component<Box2DPhysics> {
            this.maxSpeed = 2000f

            this.bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.DynamicBody
                this.bullet = true
                this.fixedRotation = true
            }

            this.fixtureDefs.add(FixtureDef().apply {
                this.filter.categoryBits = Groups.BULLETS
                this.filter.maskBits = Groups.ENEMIES.or(Groups.WALLS)

                this.friction = 0f
                this.isSensor = true
            })
        })

        entity.add(component<GroupMask> { mask = Groups.BULLETS })
        entity.add(component<Tint>())
        entity.add(component<RenderSprite> { sprite = Sprite(bulletTexture) })
        entity.add(component<Scripted> {
            scripts.add(Rotate(BULLET_ROTATION_PER_SECOND))
            scripts.add(BulletScript())
        })

        return entity
    }
}