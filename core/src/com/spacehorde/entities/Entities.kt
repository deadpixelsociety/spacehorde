package com.spacehorde.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.utils.ObjectMap
import com.badlogic.gdx.utils.Pools
import com.spacehorde.Groups
import com.spacehorde.components.*
import com.spacehorde.entities.generators.*
import com.spacehorde.graphics.Fonts
import com.spacehorde.graphics.Particles
import com.spacehorde.scripts.AlphaTween
import com.spacehorde.scripts.ChainScript
import com.spacehorde.scripts.CompositeScript
import com.spacehorde.scripts.MoveTween
import com.spacehorde.scripts.impl.DollyScript
import com.spacehorde.scripts.impl.ExplosionScript
import com.spacehorde.scripts.impl.KillScript
import kotlin.experimental.or

object Entities {
    private const val PLAYER = "player"
    private const val WALL = "wall"
    private const val BULLET = "bullet"
    private const val SHRAPNEL = "shrapnel"
    private const val ENEMY_CIRCLE = "enemy_circle"
    private const val ENEMY_CROSS = "enemy_cross"
    private const val ENEMY_DIAMOND = "enemy_diamond"
    private const val ENEMY_PINWHEEL = "enemy_pinwheel"
    private const val ENEMY_SMALL = "enemy_small"
    private const val SCORE_TEXT = "score_text"

    private val generators = ObjectMap<String, EntityGenerator>()
    private val playerNames = mutableListOf<String>()

    init {
        generators.put(PLAYER, PlayerShipGenerator())
        generators.put(WALL, WallGenerator())
        generators.put(BULLET, BulletGenerator())
        generators.put(SHRAPNEL, ShrapnelGenerator())
        generators.put(ENEMY_CIRCLE, EnemyCircleShipGenerator())
        generators.put(ENEMY_CROSS, EnemyCrossShipGenerator())
        generators.put(ENEMY_DIAMOND, EnemyDiamondShipGenerator())
        generators.put(ENEMY_PINWHEEL, EnemyPinwheelShipGenerator())
        generators.put(ENEMY_SMALL, EnemySmallShipGenerator())
        generators.put(SCORE_TEXT, ScoreTextGenerator())

        playerNames.add("MOONMOON_OW")
        playerNames.add("deadpixelsociety")
        playerNames.add("Drayaz")
        playerNames.add("ashmere")
        playerNames.add("Coconut Sriracha")
        playerNames.add("Drayaz")
        playerNames.add("LaneyBug")
        playerNames.add("Cryovr")
        playerNames.add("Sonata")
        playerNames.add("NaM")
        playerNames.add("Yung Dab")
        playerNames.add("Alabaster Slim")
        playerNames.add("Jenny Hall")
        playerNames.add("Eugene")
        playerNames.add("Billy")
        playerNames.add("Random PLeb")
        playerNames.add("God Sub")
        playerNames.add("Redditor")
        playerNames.add("Berji!")
        playerNames.add("PLEAD")
        playerNames.add("sodapoppin")
        playerNames.add("squadW")
        playerNames.add("AYAYA")
        playerNames.add("LUL")
        playerNames.add("BLAP!BLAP!")
        playerNames.add("4Head")
        playerNames.add("POGGOP")
        playerNames.add("Poggers")
        playerNames.add("Slackjack5")
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
        return enemy(engine, ENEMY_PINWHEEL) {
            getComponent(Box2DPhysics::class.java).apply {
                bodyDef.type = BodyDef.BodyType.StaticBody

                fixtureDefs[0].apply {
                    this.isSensor = true
                }
            }
        }
    }

    fun enemySmall(engine: Engine): Entity {
        return enemy(engine, ENEMY_SMALL)
    }

    private fun enemy(engine: Engine, type: String, init: (Entity.() -> Unit)? = null): Entity {
        return get(type, engine) {
            getComponent(Box2DPhysics::class.java).apply {
                fixtureDefs[0].apply {
                    this.filter.categoryBits = Groups.ENEMIES
                    this.filter.maskBits = Groups.WALLS.or(Groups.BULLETS).or(Groups.ENEMIES).or(Groups.PLAYERS)
                }
            }

            if (init != null) this.init()
        }
    }

    fun player(engine: Engine, x: Float, y: Float, first: Boolean = false): Entity {
        return get(PLAYER, engine) {
            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
            }

            getComponent(Box2DPhysics::class.java).apply {
                this.bodyDef.position.set(x, y)

                fixtureDefs[0].apply {
                    this.filter.categoryBits = Groups.PLAYERS
                    this.filter.maskBits = Groups.ENEMIES.or(Groups.WALLS).or(Groups.PLAYERS).or(Groups.SHRAPNEL)
                }
            }

            add(component<Text>(engine) {
                this.text = if (first) playerNames[0] else playerNames[MathUtils.random(1, playerNames.size - 1)]
                Fonts.glyphLayout.setText(Fonts.text, this.text)
                this.offset.set(-Fonts.glyphLayout.width * .5f, 20f)
            })
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

    fun shrapnel(engine: Engine, x: Float, y: Float): Entity {
        return get(SHRAPNEL, engine) {
            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
            }

            getComponent(Box2DPhysics::class.java).apply {
                this.bodyDef.position.set(x, y)
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
                    this.filter.maskBits = Groups.ENEMIES.or(Groups.PLAYERS).or(Groups.BULLETS).or(Groups.SHRAPNEL)

                    this.shape = PolygonShape().apply {
                        setAsBox(hw, hh, Vector2(hw, hh), 0f)
                    }
                })
            }
        }
    }

    fun explosion(engine: Engine, x: Float, y: Float, color: Color): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Transform>(engine) {
            this.position.set(x, y)
        })

        entity.add(component<ParticleOwner>(engine) {
            effect = Particles.explosion().apply {
                val tint = emitters[0].tint
                val colors = tint.colors
                colors[3] = color.r
                colors[4] = color.g
                colors[5] = color.b
                tint.colors = colors
            }

            update = { engine, entity, particleEffect ->
                particleEffect.setPosition(x, y)
            }
        })

        entity.add(component<Scripted>(engine) {
            this.scripts.add(ExplosionScript())
        })

        engine.addEntity(entity)

        return entity
    }

    fun pop(engine: Engine, x: Float, y: Float, color: Color): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Transform>(engine) {
            this.position.set(x, y)
        })

        entity.add(component<ParticleOwner>(engine) {
            effect = Particles.pop().apply {
                val tint = emitters[0].tint
                val colors = tint.colors
                colors[3] = color.r
                colors[4] = color.g
                colors[5] = color.b
                tint.colors = colors
            }

            update = { engine, entity, particleEffect ->
                particleEffect.setPosition(x, y)
            }
        })

        entity.add(component<Scripted>(engine) {
            this.scripts.add(ExplosionScript())
        })

        engine.addEntity(entity)

        return entity
    }

    fun scoreText(engine: Engine, x: Float, y: Float, value: Int, color: Color): Entity {
        return get(SCORE_TEXT, engine) {
            Fonts.glyphLayout.setText(Fonts.scoreText, value.toString())

            getComponent(Transform::class.java).apply {
                this.position.set(x, y)
                this.origin.set(Fonts.glyphLayout.width * .5f, Fonts.glyphLayout.height * .5f)
            }

            getComponent(Scripted::class.java).apply {
                this.scripts.add(ChainScript().apply {
                    this.scripts.add(CompositeScript().apply {
                        this.scripts.add(MoveTween(x, y + 20f, 1.5f))
                        this.scripts.add(AlphaTween(1f, 0f, 1.5f))
                    })

                    this.scripts.add(KillScript())
                })

            }

            getComponent(Text::class.java).apply {
                this.text = value.toString()
            }

            getComponent(Tint::class.java).apply {
                this.color.set(color)
            }

            getComponent(Size::class.java).apply {
                this.width = Fonts.glyphLayout.width
                this.height = Fonts.glyphLayout.height
            }
        }
    }

    fun camera(engine: Engine, camera: OrthographicCamera): Entity {
        val entity = Pools.obtain(Entity::class.java)

        entity.add(component<Transform>(engine))
        entity.add(component<Dolly>(engine) {
            this.camera = camera
        })

        entity.add(component<Scripted>(engine) {
            this.scripts.add(DollyScript())
        })

        engine.addEntity(entity)
        return entity
    }
}