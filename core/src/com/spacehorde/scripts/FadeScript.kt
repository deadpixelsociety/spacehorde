package com.spacehorde.scripts

class FadeScript(private val bottom: Float, private val top: Float) : ChainScript() {
    init {
        this.scripts.add(AlphaTween(1f, bottom, .25f))
        this.scripts.add(LoopScript().apply {
            this.scripts.add(AlphaTween(bottom, top, .25f))
            this.scripts.add(AlphaTween(top, bottom, .25f))
        })
    }
}