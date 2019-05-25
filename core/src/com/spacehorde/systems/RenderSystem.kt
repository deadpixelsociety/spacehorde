package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.spacehorde.components.*
import com.spacehorde.service.service

class RenderSystem(private val camera: Camera)
    : IteratingSystem(Family.all(Transform::class.java).one(RenderSprite::class.java).get()) {

    companion object {
        private const val DEBUG = true
    }

    private val spriteMapper by mapper<RenderSprite>()
    private val tintMapper by mapper<Tint>()
    private val transformMapper by mapper<Transform>()
    private val sizeMapper by mapper<Size>()
    private val batch by service<SpriteBatch>()
    private val shape by service<ShapeRenderer>()
    private var renderDebug = false
    private val v0 = Vector2()
    private val v1 = Vector2()

    override fun checkProcessing() = false

    override fun update(deltaTime: Float) {
        begin()
        super.update(deltaTime)
        end()

        if (DEBUG) {
            renderDebug = true
            beginDebug()
            super.update(deltaTime)
            endDebug()
            renderDebug = false
        }
    }

    private fun beginDebug() {
        shape.projectionMatrix = camera.combined
        shape.begin(ShapeRenderer.ShapeType.Line)
    }

    private fun endDebug() {
        shape.end()
    }

    private fun begin() {
        batch.projectionMatrix = camera.combined
        batch.begin()
        batch.setBlendFunction(GL30.GL_SRC_ALPHA, GL30.GL_ONE)
    }

    private fun end() {
        batch.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return

        val transform = transformMapper.get(entity) ?: return
        val render = spriteMapper.get(entity) ?: return
        val tint = tintMapper.get(entity)?.color ?: Color.WHITE
        val size = sizeMapper.get(entity) ?: return

        if (!renderDebug) {
            render.sprite?.apply {
                setOrigin(transform.origin.x, transform.origin.y)
                rotation = transform.angle
                setScale(transform.scale.x, transform.scale.y)
                setPosition(transform.position.x, transform.position.y)
                setColor(tint.r, tint.g, tint.b, tint.a)
                draw(batch)
            }
        }

        if (renderDebug) {
            shape.color = Color.BLUE
            shape.circle(transform.position.x, transform.position.y, 2f)

            v0.set(transform.position).add(transform.origin)
            shape.color = Color.CYAN
            shape.circle(v0.x, v0.y, 2f)

            v1.set(transform.heading).nor().scl(20f)
            shape.color = Color.RED
            shape.line(v0.x, v0.y, v0.x + v1.x, v0.y + v1.y)

            shape.color = Color.CORAL
            val sx = size.width * transform.scale.x * .5f
            val sy = size.height * transform.scale.y * .5f
            shape.box(v0.x - sx, v0.y - sy, 0f, sx * 2f, sy * 2f, 0f)

            shape.color = Color.WHITE
        }
    }
}