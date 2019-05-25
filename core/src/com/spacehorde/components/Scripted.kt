package com.spacehorde.components

import com.spacehorde.gdxArray
import com.spacehorde.scripts.Script

class Scripted : PoolableComponent() {
    val scripts = gdxArray<Script>()

    override fun reset() {
        scripts.clear()
    }
}