package com.spacehorde.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.ObjectMap
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.entities.generators.*
import kotlin.experimental.or

object Entities {
    private const val PLAYER = "player"
    private const val WALL = "wall"
    private const val BULLET = "bullet"
    private const val ENEMY_CIRCLE = "enemy_circle"
    private const val ENEMY_CROSS = "enemy_cross"
    private const val ENEMY_DIAMOND = "enemy_diamond"
    private const val ENEMY_PINWHEEL = "enemy_pinwheel"
    private const val ENEMY_SMALL = "enemy_small"

    private val generators = ObjectMap<String, EntityGenerator>()

    init {
        generators.put(PLAYER, PlayerShipGenerator())
        generators.put(WALL, WallGenerator())
        generators.put(BULLET, BulletGenerator())
        generators.put(ENEMY_CIRCLE, EnemyCircleShipGenerator())
        generators.put(ENEMY_CROSS, EnemyCrossShipGenerator())
        generators.put(ENEMY_DIAMOND, EnemyDiamondShipGenerator())
        generators.put(ENEMY_PINWHEEL, EnemyPinwheelShipGenerator())
        generators.put(ENEMY_SMALL, EnemySmallShipGenerator())
    }

    fun get(id: String, engine: Engine, init: Entity.() -> Unit): Entity {
        val entity = generators[id].generate(engine)
        entity.init()
        engine.addEntity(entity)
        return entity
    }

    fun enemyCircle(engine: Engine): Entity {
        return enemy(engine, ENEMY_CIRCLE)
    }

    fun enemyCross(engine: Engine): Entity {
        return enemy(engine, ENEMY_CROSS)
    }

    fun enemyDiamond(engine: Engine): Entity {
        return enemy(engine, ENEMY_DIAMOND)
    }

    fun enemyPinwheel(engine: Engine): Entity {
        return enemy(engine, ENEMY_PINWHEEL)
    }

    fun enemySmall(engine: Engine): Entity {
        return enemy(engine, ENEMY_SMALL)
    }

    private fun enemy(engine: Engine, type: String): Entity {
        return get(type, engine) {
            getComponent(Box2DPhysics::class.java).apply {
                fixtureDefs[0].apply {
                    this.filter.categoryBits = Groups.ENEMIES
                    this.filter.maskBits = Groups.WALLS.or(Groups.BULLETS).or(Groups.ENEMIES).or(Groups.PLAYERS)
                }
            }
        }
    }

    fun player(engine: Engine, x: Float, y: Float): Entity {
        return get(PLAYER, engine) {
            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
            }

            getComponent(Box2DPhysics::class.java).apply {
                this.bodyDef.position.set(x, y)

                fixtureDefs[0].apply {
                    this.filter.categoryBits = Groups.PLAYERS
                    this.filter.maskBits = Groups.ENEMIES.or(Groups.WALLS).or(Groups.PLAYERS)
                }
            }
        }
    }

    fun bullet(engine: Engine, x: Float, y: Float, width: Float, height: Float, color: Color): Entity {
        return get(BULLET, engine) {
            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
                this.origin.set(width * .5f, height * .5f)
            }

            getComponent(RenderSprite::class.java).apply {
                sprite?.apply {
                    this.setSize(width, height)
                    this.setOriginCenter()
                }
            }

            getComponent(Tint::class.java).apply {
                this.color.set(color)
            }

            getComponent(Box2DPhysics::class.java).apply {
                this.bodyDef.position.set(x, y)

                fixtureDefs[0].apply {
                    this.shape = CircleShape().apply {
                        this.radius = width * .5f
                    }
                }
            }
        }
    }

    fun wall(engine: Engine, x: Float, y: Float, width: Float, height: Float): Entity {
        return get(WALL, engine) {
            val hw = width * .5f
            val hh = height * .5f

            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
                this.origin.set(0f, 0f)
            }

            getComponent(Size::class.java).apply {
                this.width = width
                this.height = height
            }

            getComponent(RenderSprite::class.java).apply {
                this.sprite?.apply {
                    setSize(width, height)
                }
            }

            getComponent(Box2DPhysics::class.java).apply {
                bodyDef.position.set(x, y)

                fixtureDefs.add(FixtureDef().apply {
                    this.filter.categoryBits = Groups.WALLS
                    this.filter.maskBits = Groups.ENEMIES.or(Groups.PLAYERS).or(Groups.BULLETS)

                    this.shape = PolygonShape().apply {
                        setAsBox(hw, hh, Vector2(hw, hh), 0f)
                    }
                })
            }
        }
    }
}