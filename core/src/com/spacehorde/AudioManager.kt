package com.spacehorde

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.assets.asset

object AudioManager {
    private const val MUSIC_BASE = .7f
    private const val SOUND_BASE = .2f

    var volume = 1f
        set(value) {
            field = MathUtils.clamp(value, 0f, 1f)
            update()
        }

    private val music by asset<Music>("music/the_water_and_the_well.mp3")
    private val newShipSound by asset<Sound>("sounds/new_ship.wav")
    private val newBombSound by asset<Sound>("sounds/new_bomb.wav")
    private val bombSound by asset<Sound>("sounds/bomb.wav")
    private val shootSound by asset<Sound>("sounds/shoot.wav")
    private val explosionSound by asset<Sound>("sounds/explosion.wav")

    private fun update() {
        if (music.isPlaying) music.volume = MUSIC_BASE * volume
    }

    fun playMusic() {
        music.isLooping = true
        music.play()
    }

    fun playNewShip() {
        newShipSound.play(SOUND_BASE * volume)
    }

    fun playNewBomb() {
        newBombSound.play(SOUND_BASE * volume)
    }

    fun playBomb() {
        bombSound.play(SOUND_BASE * volume)
    }

    fun playShoot(jitter: Float) {
        shootSound.play(SOUND_BASE * volume, 1f + (MathUtils.random(-jitter, jitter) + MathUtils.random(-jitter, jitter)), 0f)
    }

    fun playExplosion() {
        explosionSound.play(SOUND_BASE * volume)
    }
}