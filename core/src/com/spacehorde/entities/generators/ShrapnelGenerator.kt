package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.utils.Pools
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.scripts.Rotate
import com.spacehorde.scripts.impl.ShrapnelScript
import kotlin.experimental.or

class ShrapnelGenerator : EntityGenerator {
    companion object {
        private const val BULLET_ROTATION_PER_SECOND = 540f
    }

    private val shrapnelTexture by asset<Texture>("textures/shrapnel.png")

    override fun generate(engine: Engine): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Spatial>(engine))
        entity.add(component<Transform>(engine) {
            this.origin.set(shrapnelTexture.width * .5f, shrapnelTexture.height * .5f)
        })

        entity.add(component<Size>(engine) {
            this.width = shrapnelTexture.width.toFloat()
            this.height = shrapnelTexture.height.toFloat()
        })

        entity.add(component<Box2DPhysics>(engine) {
            this.maxSpeed = 2000f

            this.bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.DynamicBody
                this.bullet = true
                this.fixedRotation = true
            }

            this.fixtureDefs.add(FixtureDef().apply {
                this.filter.categoryBits = Groups.SHRAPNEL
                this.filter.maskBits = Groups.PLAYERS.or(Groups.WALLS)

                this.friction = 0f
                this.isSensor = true

                this.shape = CircleShape().apply {
                    this.radius = shrapnelTexture.width * .5f
                }
            })
        })

        entity.add(component<GroupMask>(engine) { mask = Groups.SHRAPNEL })
        entity.add(component<Tint>(engine))
        entity.add(component<RenderSprite>(engine) { sprite = Sprite(shrapnelTexture) })
        entity.add(component<Scripted>(engine) {
            scripts.add(Rotate(BULLET_ROTATION_PER_SECOND))
            scripts.add(ShrapnelScript())
        })

        return entity
    }
}