package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.utils.Pools
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*

class WallGenerator : EntityGenerator {
    private val pixel by asset<Texture>("textures/pixel.png")

    override fun generate(engine: Engine): Entity {
        val entity = Pools.obtain(Entity::class.java)
        entity.add(component<GroupMask>(engine) { mask = Groups.WALLS })
        entity.add(component<Transform>(engine))
        entity.add(component<Size>(engine))
        entity.add(component<Tint>(engine) { color.set(.2f, 0f, .8f, 1f) })
        entity.add(component<RenderSprite>(engine) { sprite = Sprite(pixel) })

        entity.add(component<Box2DPhysics>(engine) {
            bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.StaticBody
                this.fixedRotation = true
            }
        })

        return entity
    }
}