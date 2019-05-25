package com.spacehorde.scripts

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.spacehorde.gdxArray

open class ChainScript : Script() {
    /**
     * The child scripts.
     */
    val scripts = gdxArray<Script>()

    override fun update(deltaTime: Float, engine: Engine, entity: Entity): Boolean {
        if (scripts.size == 0) return true

        val script = scripts[0]
        if (!script.started) {
            script.start(engine, entity)
            script.started = true
        }

        if (script.update(deltaTime, engine, entity)) {
            script.finish(engine, entity)
            scripts.removeIndex(0)
        }

        return scripts.size == 0
    }

    override fun reset() {
        scripts.clear()
    }
}