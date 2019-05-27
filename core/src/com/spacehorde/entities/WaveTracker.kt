package com.spacehorde.entities

import com.badlogic.gdx.math.MathUtils

object WaveTracker {
    private const val TARGET_TIME_MINUTES = 60f * 10f // max difficulty at 10 minutes
    private const val WAVE_DELAY_SECONDS = 3f
    private const val FIRST_SPAWN_DELAY_SECONDS = 8f

    var difficulty = 0f
        set(value) {
            field = MathUtils.clamp(value + addedDifficulty, 0f, 1f)
        }

    var addedDifficulty = 0f

    private var totalTime = 0f
    private var lastWave = 0f
    private var waveStarted = false

    fun update(dt: Float) {
        totalTime += dt
        difficulty = totalTime / TARGET_TIME_MINUTES
    }

    fun startWave() {
        waveStarted = true
    }

    fun finishWave() {
        if (!waveStarted) return
        waveStarted = false
        lastWave = totalTime
    }

    fun canSpawnWave() = (totalTime >= FIRST_SPAWN_DELAY_SECONDS) && !waveStarted && (totalTime - lastWave) >= WAVE_DELAY_SECONDS

    fun difficultyLevel(): Difficulty {
        return when ((difficulty * 100).toInt()) {
            in 0..5 -> Difficulty.REDDIT_USER
            in 6..15 -> Difficulty.PLEB
            in 16..25 -> Difficulty.NEW_FRIEND
            in 26..35 -> Difficulty.PRIME_PLEB
            in 36..55 -> Difficulty.SUB
            in 56..65 -> Difficulty.MOD
            in 66..75 -> Difficulty.STAFF
            in 76..85 -> Difficulty.RP_GOD
            else -> Difficulty.GOD_GAMER
        }
    }

    fun reset() {
        totalTime = 0f
        waveStarted = false
        lastWave = 0f
        difficulty = 0f
    }
}