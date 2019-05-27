package com.spacehorde.systems

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.crashinvaders.vfx.PostProcessor
import com.crashinvaders.vfx.effects.BloomEffect
import com.spacehorde.Groups
import com.spacehorde.assets.asset
import com.spacehorde.components.*
import com.spacehorde.service.service


class RenderSystem(private val camera: Camera)
    : ContainerSystem(Family.all(Transform::class.java).one(RenderSprite::class.java, ParticleOwner::class.java).get()) {

    private val spriteMapper by mapper<RenderSprite>()
    private val tintMapper by mapper<Tint>()
    private val transformMapper by mapper<Transform>()
    private val particleMapper by mapper<ParticleOwner>()
    private val batch by service<SpriteBatch>()
    private val groupSystem by lazy { engine.getSystem(GroupSystem::class.java) }
    private val postProcessor = PostProcessor(Pixmap.Format.RGBA8888)
    private val bloom = BloomEffect()
    private val starsBG by asset<Texture>("textures/stars.png")
    private val starSprite by lazy {
        Sprite(starsBG).apply {
            setOriginCenter()
            setOriginBasedPosition(0f, 0f)
        }
    }

    override fun checkProcessing() = false

    override fun addedToEngine(engine: Engine?) {
        super.addedToEngine(engine)
        postProcessor.resize(Gdx.graphics.width, Gdx.graphics.height)

        bloom.blurPasses = 50
        bloom.setBloomIntesity(1.3f)

        postProcessor.addEffect(bloom)
    }

    override fun removedFromEngine(engine: Engine?) {
        super.removedFromEngine(engine)
        bloom.dispose()
        postProcessor.dispose()
    }

    override fun onEntityRemoved(entity: Entity) {
        super.onEntityRemoved(entity)
        val particle = particleMapper.get(entity) ?: return
        val pooledEffect = particle.effect as? ParticleEffectPool.PooledEffect
        pooledEffect?.free()
        particle.effect = null
    }

    override fun update(deltaTime: Float) {
        batch.projectionMatrix = camera.combined

        postProcessor.beginCapture()
        batch.begin()
        starSprite.draw(batch)
        batch.end()

        renderWalls()
        renderBullets()
        renderParticles(deltaTime)
        renderEnemies()
        renderPlayers()
        postProcessor.endCapture()

        postProcessor.isBlendingEnabled = true
        postProcessor.render()
    }

    private fun renderParticles(deltaTime: Float) {
        batch.enableBlending()
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE)
        batch.begin()
        val particleOwners = entities().filter { it.has<ParticleOwner>() }
        particleOwners.forEach {
            val particle = particleMapper.get(it)
            val effect = particle.effect
            if (effect != null) {
                // This should probably but in a separate system but fuck I'm tired.
                particle.update(engine, it, effect)
                effect.update(deltaTime)
                effect.draw(batch)
            }
        }

        batch.end()
        batch.enableBlending()
    }

    private fun renderPlayers() {
        batch.enableBlending()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.begin()
        val players = groupSystem[Groups.PLAYERS]
        players.forEach { player -> renderSprite(player) }
        batch.end()
        batch.enableBlending()
    }

    private fun renderEnemies() {
        batch.enableBlending()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        batch.begin()
        val enemies = groupSystem[Groups.ENEMIES]
        enemies.forEach { enemy -> renderSprite(enemy) }
        batch.end()
        batch.enableBlending()
    }

    private fun renderBullets() {
        batch.enableBlending()
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE)
        batch.begin()
        val bullets = groupSystem[Groups.BULLETS]
        bullets.forEach { bullet -> renderSprite(bullet) }
        val shrapnel = groupSystem[Groups.SHRAPNEL]
        shrapnel.forEach { piece -> renderSprite(piece) }
        batch.end()
        batch.enableBlending()
    }

    private fun renderWalls() {
        batch.disableBlending()
        batch.begin()
        val walls = groupSystem[Groups.WALLS]
        walls.forEach { wall -> renderSprite(wall) }
        batch.end()
        batch.enableBlending()
    }

    private fun renderSprite(entity: Entity) {
        val transform = transformMapper.get(entity) ?: return
        val render = spriteMapper.get(entity) ?: return
        val tint = tintMapper.get(entity)?.color ?: Color.WHITE

        render.sprite?.apply {
            setOrigin(transform.origin.x, transform.origin.y)
            rotation = transform.angle
            setScale(transform.scale.x, transform.scale.y)
            setPosition(transform.position.x, transform.position.y)
            setColor(tint.r, tint.g, tint.b, tint.a)
            draw(batch)
        }
    }
}