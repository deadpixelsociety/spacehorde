package com.spacehorde.graphics

import com.badlogic.gdx.graphics.Color

// Adopted from java.awt.Color
fun HSB.toColor(alpha: Float = 1f): Color {
    if (saturation == 0f) return Color(brightness, brightness, brightness, 1f)

    var r = 0f
    var g = 0f
    var b = 0f

    val h = ((hue / 360f) - Math.floor((hue / 360f).toDouble()).toFloat()) * 6f
    val f = h - Math.floor(h.toDouble()).toFloat()
    val p = brightness * (1f - saturation)
    val q = brightness * (1f - saturation * f)
    val t = brightness * (1f - saturation * (1f - f))

    when (h.toInt()) {
        0 -> {
            r = brightness
            g = t
            b = p
        }
        1 -> {
            r = q
            g = brightness
            b = p
        }
        2 -> {
            r = p
            g = brightness
            b = t
        }
        3 -> {
            r = p
            g = q
            b = brightness
        }
        4 -> {
            r = t
            g = p
            b = brightness
        }
        5 -> {
            r = brightness
            g = p
            b = q
        }
    }

    return Color(r, g, b, alpha)
}

// Adopted from java.awt.Color
fun Color.toHSB(): HSB {
    val max = if (r > g) if (r > b) r else b else g
    val min = if (r < g) if (r < b) r else b else g

    val b = max

    val s = if (max != 0f) (max - min) / max else 0f
    if (s == 0f) return HSB(0f, 0f, b)

    val rc = (max - r) / (max - min)
    val gc = (max - g) / (max - min)
    val bc = (max - b) / (max - min)

    var h = (if (r == max) bc - gc
    else if (g == max) 2f + rc - bc
    else 4f + gc - rc) / 6f

    if (h < 0f) h += 1f

    return HSB(h, s, b)
}
