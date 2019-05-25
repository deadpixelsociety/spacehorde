package com.spacehorde.ships

import com.spacehorde.graphics.HSB
import com.spacehorde.graphics.toColor

data class ShipColor(val hue: Float) {
    val cockpitColor = HSB(hue, .6f, 1f).toColor()
    val bodyColor = HSB(0f, 0f, .95f).toColor()
    val hullColor = HSB(hue, .8f, .6f).toColor()
}