package com.spacehorde.scripts.impl

import com.spacehorde.scripts.AlphaTween
import com.spacehorde.scripts.ChainScript
import com.spacehorde.scripts.LoopScript

class FadeScript(private val bottom: Float, private val top: Float) : ChainScript() {
    init {
        this.scripts.add(AlphaTween(0f, bottom, .25f))
        this.scripts.add(LoopScript().apply {
            this.scripts.add(AlphaTween(bottom, top, .25f))
            this.scripts.add(AlphaTween(top, bottom, .25f))
        })
    }
}