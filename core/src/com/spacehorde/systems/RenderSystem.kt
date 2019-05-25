package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.spacehorde.components.RenderSprite
import com.spacehorde.components.Tint
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.service.service

class RenderSystem(private val camera: Camera)
    : IteratingSystem(Family.all(Transform::class.java).one(RenderSprite::class.java).get()) {

    private val spriteMapper by mapper<RenderSprite>()
    private val tintMapper by mapper<Tint>()
    private val transformMapper by mapper<Transform>()
    private val batch by service<SpriteBatch>()

    override fun checkProcessing() = false

    override fun update(deltaTime: Float) {
        begin()
        super.update(deltaTime)
        end()
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