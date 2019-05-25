package com.spacehorde.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.components.*
import com.spacehorde.graphics.HSB
import com.spacehorde.graphics.toColor
import com.spacehorde.ships.PlayerShipMask
import com.spacehorde.ships.ShipMask
import com.spacehorde.usingWith

object Entities {
    private const val SHIP_HEIGHT = 16
    private const val SHIP_WIDTH = 16

    fun createPlayerShip(x: Float, y: Float): Entity {
        val entity = createShip(x, y, PlayerShipMask())
        entity.add(component<Tag> { id = Tag.PLAYER })
        return entity
    }

    private fun createShip(x: Float, y: Float, shipMask: ShipMask): Entity {
        val entity = Entity()

        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(SHIP_WIDTH * .5f, SHIP_HEIGHT * .5f)
        })

        entity.add(component<Size> {
            width = SHIP_WIDTH.toFloat()
            height = SHIP_HEIGHT.toFloat()
        })

        entity.add(component<Physics> {
            maxSpeed = 300f
            rotationSpeed = .075f
            accelerationSpeed = 200f
        })

        entity.add(component<Tint>())
        entity.add(component<RenderSprite> { sprite = createShipSprite(shipMask) })

        return entity
    }

    private fun createShipSprite(shipMask: ShipMask): Sprite {
        val randomMask = shipMask.generateRandomFullMask()

        val cockpitHue = MathUtils.random() * 360f
        val cockpitColor = HSB(cockpitHue, .6f, 1f).toColor()
        val bodyColor = HSB(0f, 0f, .95f).toColor()
        val hullColor = HSB(cockpitHue, .8f, .6f).toColor()

        return Pixmap(SHIP_WIDTH, SHIP_HEIGHT, Pixmap.Format.RGBA8888).usingWith {
            for (y in 0 until SHIP_HEIGHT) {
                for (x in 0 until SHIP_WIDTH) {
                    val cell = randomMask[x, y]

                    it.setColor(when (cell) {
                        ShipMask.Cell.EMPTY -> Color.CLEAR
                        ShipMask.Cell.BODY -> bodyColor
                        ShipMask.Cell.COCKPIT -> cockpitColor
                        ShipMask.Cell.HULL -> hullColor
                    })

                    it.drawPixel(x, y)
                }
            }

            Sprite(Texture(it))
        }
    }
}