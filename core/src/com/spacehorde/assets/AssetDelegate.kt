package com.spacehorde.assets

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.spacehorde.Action
import com.spacehorde.service.service
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class AssetDelegate<T : Any, P : AssetLoaderParameters<T>>(
        private val filename: String,
        private val assetClass: Class<T>,
        private val parameters: P? = null,
        private val init: Action<T>? = null) : ReadOnlyProperty<Any, T> {

    private val assetManager by service<AssetManager>()
    private lateinit var asset: T

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (!::asset.isInitialized) asset = loadAsset()
        return asset
    }

    private fun loadAsset(): T {
        if (!assetManager.isLoaded(filename, assetClass)) {
            assetManager.load(filename, assetClass, parameters)
            assetManager.finishLoading()
        }

        return assetManager.get(filename, assetClass)?.apply { init?.invoke(this) }
                ?: throw Exception("Asset $filename not found.")
    }
}