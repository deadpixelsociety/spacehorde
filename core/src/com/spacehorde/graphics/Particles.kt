package com.spacehorde.graphics

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener
import com.badlogic.gdx.graphics.g2d.ParticleEffect
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool

object Particles : LifecycleListener {
    private lateinit var exhaustPool: ParticleEffectPool
    private lateinit var explosionPool: ParticleEffectPool
    private lateinit var popPool: ParticleEffectPool

    fun load() {
        val exhaustEffect = ParticleEffect().apply {
            load(Gdx.files.internal("particles/exhaust.p"), Gdx.files.internal("particles/textures/"))
        }

        exhaustPool = ParticleEffectPool(exhaustEffect, 5, 1000)

        val explosionEffect = ParticleEffect().apply {
            load(Gdx.files.internal("particles/explosion.p"), Gdx.files.internal("particles/textures/"))
        }

        explosionPool = ParticleEffectPool(explosionEffect, 5, 1000)

        val popEffect = ParticleEffect().apply {
            load(Gdx.files.internal("particles/pop.p"), Gdx.files.internal("particles/textures/"))
        }

        popPool = ParticleEffectPool(popEffect, 20, 1000)
    }

    fun exhaust() = exhaustPool.obtain()

    fun explosion() = explosionPool.obtain()

    fun pop() = popPool.obtain()

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        exhaustPool.clear()
        explosionPool.clear()
        popPool.clear()
    }
}