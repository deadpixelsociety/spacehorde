package com.spacehorde.entities.generators

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.utils.Pools
import com.spacehorde.components.*
import com.spacehorde.graphics.Particles
import com.spacehorde.ships.ShipColor
import com.spacehorde.ships.ShipMask
import com.spacehorde.usingWith

abstract class ShipGenerator : EntityGenerator {
    protected fun createRandomShip(engine: Engine, shipMask: ShipMask, shipHue: Float): Entity {

        val shipColor = ShipColor(shipHue)
        return createShip(engine, shipMask.width * 2f, shipMask.height.toFloat()).apply {
            add(component<Meta>(engine) { put("ShipColor", shipColor) })

            getComponent(RenderSprite::class.java).apply {
                this.sprite = createMaskedSprite(shipMask, shipColor)
            }

            add(component<ParticleOwner>(engine) {
                effect = Particles.exhaust()
                update = { engine, entity, effect ->
                    val transform = entity.getComponent(Transform::class.java)
                    effect.setPosition(transform.position.x + transform.origin.x, transform.position.y + transform.origin.y)
                    val angle = transform.angle - 90f

                    val angleRange = effect.emitters[0].angle
                    val angleHighMin = angleRange.highMin
                    val angleHighMax = angleRange.highMax
                    val angleSpanHigh = angleHighMax - angleHighMin
                    angleRange.setHigh(angle - angleSpanHigh * .5f, angle + angleSpanHigh * .5f)

                    val angleLowMin = angleRange.lowMin
                    val angleLowMax = angleRange.lowMax
                    val angleSpanLow = angleLowMax - angleLowMin
                    angleRange.setLow(angle - angleSpanLow * .5f, angle + angleSpanLow * .5f)

                    val physics = entity.getComponent(Box2DPhysics::class.java)
                    val speed = physics.body?.linearVelocity?.len() ?: 0f
                    val delta = speed / 200f
                    val velocityRange = effect.emitters[0].velocity
                    val velHighMin = velocityRange.highMin
                    val velHighMax = velocityRange.highMax
                    val velLowMin = velocityRange.lowMin
                    val velLowMax = velocityRange.lowMax

                    velocityRange.setHigh(velHighMin * delta, velHighMax * delta)
                    velocityRange.setLow(velLowMin * delta, velLowMax * delta)

                    effect.emitters[0].tint.colors
                }
            })
        }
    }

    protected fun createTextureShip(engine: Engine, texture: Texture): Entity {

        return createShip(engine, texture.width.toFloat(), texture.height.toFloat()).apply {
            add(component<Meta>(engine) { put("ShipColor", Color.WHITE) })

            getComponent(RenderSprite::class.java).apply {
                this.sprite = Sprite(texture).apply {
                    this.setSize(width, height)
                    this.setOriginCenter()
                }
            }

            getComponent(Tint::class.java).apply {
                this.color.a = 0f
            }
        }
    }

    private fun createShip(engine: Engine, width: Float, height: Float): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Spatial>(engine))
        entity.add(component<Tint>(engine))

        entity.add(component<Transform>(engine) { origin.set(width * .5f, height * .5f) })

        entity.add(component<Size>(engine) {
            this.width = width
            this.height = height
        })

        entity.add(component<RenderSprite>(engine))

        entity.add(component<Box2DPhysics>(engine) {
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