package com.spacehorde.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.graphics.HSB
import com.spacehorde.graphics.toColor
import com.spacehorde.ships.PlayerShipMask
import com.spacehorde.ships.ShipMask
import com.spacehorde.usingWith

object Entities {
    private val bulletTexture by asset<Texture>("textures/bullet.png")

    fun createPlayerShip(x: Float, y: Float): Entity {
        val entity = createShip(x, y, PlayerShipMask())
        entity.add(component<Tag> { id = Tag.PLAYER })
        return entity
    }

    fun createBullet(x: Float, y: Float): Entity {
        val entity = Entity()

        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(2.5f, 2.5f)
            scale.set(3f, 3f)
        })

        entity.add(component<Size> {
            width = 5f
            height = 5f
        })

        entity.add(component<Physics> {
            maxSpeed = 800f
            rotationSpeed = .05f
            accelerationSpeed = 400f
            frictionless = true
        })

        entity.add(component<GroupMask> { mask = GroupMask.BULLETS })
        entity.add(component<Tint>())
        entity.add(component<RenderSprite> { sprite = Sprite(bulletTexture) })

        return entity
    }

    private fun createShip(x: Float, y: Float, shipMask: ShipMask): Entity {
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

        return Pixmap(shipMask.width * 2, shipMask.height, Pixmap.Format.RGBA8888).usingWith {
            for (y in 0 until shipMask.width * 2) {
                for (x in 0 until shipMask.height) {
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