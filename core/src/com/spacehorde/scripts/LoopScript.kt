package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.gdxArray

open class LoopScript : Script() {
    val scripts = gdxArray<Script>()

    private var currentScript = 0

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (scripts.size == 0) return true

        val script = scripts[currentScript]
        if (!script.started) {
            script.start(engine, entity)
            script.started = true
        }

        if (script.update(deltaTime, engine, entity)) {
            script.finish(engine, entity)
            script.started = false
            currentScript = (currentScript + 1) % scripts.size
        }

        return scripts.size == 0
    }

    override fun reset() {
        scripts.clear()
    }
}