package com.spacehorde.scoring

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.LifecycleListener

object ScoreKeeper : LifecycleListener {
    const val NEW_SHIP_SCORE = 2000
    const val NEW_BOMB_SCORE = 5000

    var lastNewShip = 0
    var lastNewBomb = 0
    var newShipEvent: () -> Unit = {}
    var newBombEvent: () -> Unit = {}

    var highScore = 0
    var score = 0
        set(value) {
            if (value > highScore) highScore = value
            field = value

            if (value - lastNewShip >= NEW_SHIP_SCORE) {
                lastNewShip = value
                newShipEvent()
            }

            if (value - lastNewBomb >= NEW_BOMB_SCORE) {
                lastNewBomb = value
                newBombEvent()
            }
        }

    fun load() {
        if (Gdx.files.isLocalStorageAvailable) {
            try {
                val file = Gdx.files.external("highscore.txt")
                if (file.exists()) {
                    highScore = file.readString().toIntOrNull() ?: 0
                }
            } catch (ex: Exception) {
            }
        }
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
        if (Gdx.files.isExternalStorageAvailable) {
            try {
                val file = Gdx.files.external("highscore.txt")
                file.writeString(highScore.toString(), false)
            } catch (ex: Exception) {
            }
        }
    }

    fun reset() {
        lastNewBomb = 0
        lastNewShip = 0
        score = 0
    }
}