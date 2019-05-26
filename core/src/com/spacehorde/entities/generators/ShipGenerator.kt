package com.spacehorde.entities.generators

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
    protected fun createRandomShip(shipMask: ShipMask, shipHue: Float): Entity {
        val shipColor = ShipColor(shipHue)
        return createShip(shipMask.width * 2f, shipMask.height.toFloat()).apply {
            add(component<Meta> { put("ShipColor", shipColor) })

            getComponent(RenderSprite::class.java).apply {
                this.sprite = createMaskedSprite(shipMask, shipColor)
            }
        }
    }

    protected fun createTextureShip(texture: Texture): Entity {
        return createShip(texture.width.toFloat(), texture.height.toFloat()).apply {
            add(component<Meta> { put("ShipColor", Color.WHITE) })

            getComponent(RenderSprite::class.java).apply {
                this.sprite = Sprite(texture).apply {
                    this.setSize(width, height)
                    this.setOriginCenter()
                }
            }
        }
    }

    protected fun createShip(width: Float, height: Float): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Tint>())

        entity.add(component<Transform> { origin.set(width * .5f, height * .5f) })

        entity.add(component<Size> {
            this.width = width
            this.height = height
        })

        entity.add(component<RenderSprite>())

        entity.add(component<Box2DPhysics> {
            bodyDef = BodyDef().apply {
                this.type = BodyDef.BodyType.DynamicBody
                this.fixedRotation = true
            }

            fixtureDefs.add(FixtureDef().apply {
                this.restitution = .8f
                this.shape = CircleShape().apply {
                    this.radius = width * .5f * .8f
                }
            })

            manualVelocity = true
        })

        return entity
    }

    private fun createMaskedSprite(shipMask: ShipMask, shipColor: ShipColor): Sprite {
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