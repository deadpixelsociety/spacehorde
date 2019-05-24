package com.spacehorde.scene

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Disposable

class SceneContainer : Disposable {
    private val scenes = mutableListOf<Scene>()
    private val scenesToRemove = mutableListOf<Scene>()

    fun add(scene: Scene) {
        scene.create()
        scene.resize(Gdx.graphics.width, Gdx.graphics.height)
        scenes.add(scene)
    }

    private fun remove(scene: Scene) {
        scene.dispose()
        scenes.remove(scene)
    }

    fun resize(width: Int, height: Int) {
        scenes.forEach { it.resize(width, height) }
    }

    fun pause() {
        scenes.forEach { it.pause() }
    }

    fun resume() {
        scenes.forEach { it.resume() }
    }

    fun update(dt: Float, tt: Float) {
        scenesToRemove.clear()

        var top = true
        for (i in scenes.count() - 1 downTo 0) {
            val scene = scenes[i]
            scene.update(dt, tt)
            if (!top) scenesToRemove.add(scene)
            if (!scene.overlay) top = false
        }

        scenesToRemove.forEach { remove(it) }
    }

    fun draw() {
        scenes.forEach { it.draw() }
    }

    override fun dispose() {
        scenes.forEach { it.dispose() }
    }
}