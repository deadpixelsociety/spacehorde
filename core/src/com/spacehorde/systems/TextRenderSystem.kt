package com.spacehorde.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.spacehorde.components.Text
import com.spacehorde.components.Tint
import com.spacehorde.components.Transform
import com.spacehorde.components.mapper
import com.spacehorde.service.service

class TextRenderSystem(private val worldCamera: Camera, private val uiCamera: Camera)
    : IteratingSystem(Family.all(Transform::class.java, Text::class.java).get()) {

    private val textMapper by mapper<Text>()
    private val tintMapper by mapper<Tint>()
    private val transformMapper by mapper<Transform>()
    private val batch by service<SpriteBatch>()
    private val v0 = Vector3()

    override fun checkProcessing() = false

    override fun update(deltaTime: Float) {
        begin()
        super.update(deltaTime)
        end()
    }

    private fun begin() {
        batch.projectionMatrix = uiCamera.combined
        batch.enableBlending()
        batch.setBlendFunction(GL20.GL_ONE, GL20.GL_BLEND_SRC_ALPHA)
        batch.begin()
    }

    private fun end() {
        batch.end()
    }

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (entity == null) return

        val transform = transformMapper.get(entity) ?: return
        val text = textMapper.get(entity) ?: return
        val tint = tintMapper.get(entity)?.color

        v0.set(transform.position.x, transform.position.y, 0f).add(transform.origin.x, transform.origin.y, 0f)
        v0.set(worldCamera.project(v0))
        v0.add(text.offset.x, text.offset.y, 0f)

        text.font.color = tint
        text.font.draw(batch, text.text, v0.x, v0.y)
        text.font.color = Color.WHITE
    }
}