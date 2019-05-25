package com.spacehorde.assets

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.spacehorde.Action

inline fun <reified T : Any> asset(
        filename: String,
        parameters: AssetLoaderParameters<T>? = null,
        noinline init: Action<T>? = null) = AssetDelegate(
        filename,
        T::class.java,
        parameters,
        init
)

fun shader(vertFileName: String, fragFileName: String? = null) = asset<ShaderProgram>(
        vertFileName,
        ShaderProgramLoader.ShaderProgramParameter().apply {
            this.vertexFile = vertFileName
            this.fragmentFile = fragFileName
        }
) {
    if (!it.isCompiled) {
        error("Shader not compilted: ${it.log}")
    }

    if (it.log.isNotEmpty()) println("Shader log: ${it.log}")
}
