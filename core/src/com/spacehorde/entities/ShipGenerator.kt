package com.spacehorde.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.spacehorde.components.*
import com.spacehorde.ships.ShipColor
import com.spacehorde.ships.ShipMask
import com.spacehorde.usingWith

abstract class ShipGenerator : EntityGenerator {
    protected fun createShip(shipMask: ShipMask, shipHue: Float): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Tint>())

        entity.add(component<Transform> { origin.set(shipMask.width.toFloat(), shipMask.height * .5f) })

        entity.add(component<Size> {
            width = shipMask.width * 2f
            height = shipMask.height.toFloat()
        })

        val shipColor = ShipColor(shipHue)
        entity.add(component<Meta> { put("ShipColor", shipColor) })
        entity.add(component<RenderSprite> { sprite = createShipSprite(shipMask, shipColor) })

        entity.add(component<Box2DPhysics> {
            bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.DynamicBody
                this.fixedRotation = true
            }

            fixtureDefs.add(FixtureDef().apply {
                this.restitution = .8f
                this.shape = CircleShape().apply {
                    this.radius = shipMask.width.toFloat() * .8f
                }
            })

            manualVelocity = true
        })

        return entity
    }

    private fun createShipSprite(shipMask: ShipMask, shipColor: ShipColor): Sprite {
        val randomMask = shipMask.generateRandomFullMask()

        return Pixmap(shipMask.width * 2, shipMask.height, Pixmap.Format.RGBA8888).usingWith {
            for (y in 0 until shipMask.width * 2) {
                for (x in 0 until shipMask.height) {
                    val cell = randomMask[x, y]

                    it.setColor(when (cell) {
                        ShipMask.Cell.EMPTY -> Color.CLEAR
                        ShipMask.Cell.BODY -> shipColor.bodyColor
                        ShipMask.Cell.COCKPIT -> shipColor.cockpitColor
                        ShipMask.Cell.HULL -> shipColor.hullColor
                    })

                    it.drawPixel(x, y)
                }
            }

            Sprite(Texture(it))
        }
    }
}