package com.spacehorde.weapons

import com.spacehorde.scripts.Duration

abstract class WeaponDefImpl : WeaponDef {
    override val duration = Duration.INFINITE

    override val fireDelay = 1f / 10f

    override var lastFired = 0f
}