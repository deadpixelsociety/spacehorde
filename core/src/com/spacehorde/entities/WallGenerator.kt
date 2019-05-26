package com.spacehorde.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*

class WallGenerator : EntityGenerator {
    private val pixel by asset<Texture>("textures/pixel.png")

    override fun generate(engine: Engine): Entity {
        val entity = Entity()
        entity.add(component<GroupMask> { mask = Groups.WALLS })
        entity.add(component<Transform>())
        entity.add(component<Size>())
        entity.add(component<Tint> { color.set(Color.LIGHT_GRAY) })
        entity.add(component<RenderSprite> { sprite = Sprite(pixel) })

        entity.add(component<Box2DPhysics> {
            bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.StaticBody
                this.fixedRotation = true
            }
        })

        return entity
    }
}