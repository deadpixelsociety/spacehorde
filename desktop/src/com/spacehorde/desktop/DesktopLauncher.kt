package com.spacehorde.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.spacehorde.SpaceHordeGame

fun main(args: Array<String>) {
    LwjglApplication(SpaceHordeGame(), LwjglApplicationConfiguration().apply {
        title = "SpaceHorde"
        width = 1280
        height = 720
        useGL30 = false
    })
}
