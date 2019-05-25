package com.spacehorde.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.components.*
import com.spacehorde.ships.PlayerShipMask
import com.spacehorde.ships.ShipColor
import com.spacehorde.ships.ShipMask
import com.spacehorde.usingWith
import com.spacehorde.weapons.SingleBullet

object Entities {
    fun createPlayerShip(x: Float, y: Float): Entity {
        val entity = createShip(x, y, PlayerShipMask(), MathUtils.random() * 360f)
        entity.add(component<Tag> { id = Tag.PLAYER })
        entity.add(component<Weaponized> { add(SingleBullet()) })
        return entity
    }

    private fun createShip(x: Float, y: Float, shipMask: ShipMask, shipHue: Float): Entity {
        val entity = Entity()

        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(8f, 8f)
        })

        entity.add(component<Size> {
            width = 16f
            height = 16f
        })

        entity.add(component<Physics> {
            maxSpeed = 300f
            rotationSpeed = .075f
            accelerationSpeed = 200f
        })

        val shipColor = ShipColor(shipHue)
        entity.add(component<Meta> {
            put("ShipColor", shipColor)
        })

        entity.add(component<Tint>())
        entity.add(component<RenderSprite> { sprite = createShipSprite(shipMask, shipColor) })

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