package com.spacehorde.entities

import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.ships.*
import com.spacehorde.usingWith
import com.spacehorde.weapons.SingleBullet
import com.spacehorde.weapons.SmolBullet

object Entities {
    private val pixel by asset<Texture>("textures/pixel.png")

    fun createWall(x: Float, y: Float, width: Float, height: Float): Entity {
        val entity = Entity()
        entity.add(component<GroupMask> { mask = GroupMask.WALLS })
        entity.add(component<Collision> { mask = GroupMask.PLAYERS.or(GroupMask.ENEMIES).or(GroupMask.BULLETS) })
        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(width * .5f, height * .5f)
        })

        entity.add(component<Size> {
            this.width = width
            this.height = height
        })

        entity.add(component<Tint> { color.set(Color.LIGHT_GRAY) })
        entity.add(component<RenderSprite> {
            sprite = Sprite(pixel).apply {
                setSize(width, height)
            }
        })

        return entity
    }

    fun createPlayerShip(x: Float, y: Float): Entity {
        val entity = createShip(x, y, PlayerShipMask(), MathUtils.random() * 360f)
        entity.add(component<GroupMask> { mask = GroupMask.PLAYERS })
        entity.add(component<Collision> { mask = GroupMask.WALLS.or(GroupMask.ENEMIES) })
        entity.add(component<Weaponized> { add(SingleBullet()) })
        entity.getComponent(Physics::class.java).apply {
            maxSpeed = 600f
            rotationSpeed = .075f
            accelerationSpeed = 400f
        }

        return entity
    }

    fun createDiamondEnemy(x: Float, y: Float): Entity {
        val entity = createShip(x, y, DiamondShipMask(), 180f)
        entity.add(component<GroupMask> { mask = GroupMask.ENEMIES })
        entity.add(component<Collision> { mask = GroupMask.WALLS.or(GroupMask.PLAYERS).or(GroupMask.BULLETS) })
        entity.getComponent(Physics::class.java).apply {
            maxSpeed = 300f
            rotationSpeed = .075f
            accelerationSpeed = 200f
        }

        return entity
    }

    fun createSmolEnemy(x: Float, y: Float): Entity {
        val entity = createShip(x, y, SmolShipMask(), 90f)
        entity.add(component<GroupMask> { mask = GroupMask.ENEMIES })
        entity.add(component<Weaponized> { add(SmolBullet()) })
        entity.getComponent(Physics::class.java).apply {
            maxSpeed = 300f
            rotationSpeed = .075f
            accelerationSpeed = 200f
        }

        return entity
    }

    private fun createShip(x: Float, y: Float, shipMask: ShipMask, shipHue: Float): Entity {
        val entity = Entity()

        entity.add(component<Spatial>())
        entity.add(component<Transform> {
            position.set(x, y)
            origin.set(shipMask.width.toFloat(), shipMask.height * .5f)
        })

        entity.add(component<Size> {
            width = shipMask.width * 2f
            height = shipMask.height.toFloat()
        })

        entity.add(component<Physics>())

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