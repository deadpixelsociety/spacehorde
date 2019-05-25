package com.spacehorde.components

import com.spacehorde.weapons.WeaponDef

class Weaponized : PoolableComponent() {
    private val weapons = mutableListOf<WeaponDef>()

    fun weapons(): List<WeaponDef> = weapons

    fun <T : WeaponDef> add(weaponDef: T) {
        if (weapons.filterIsInstance(weaponDef.javaClass).any()) return
        weapons.add(weaponDef)
    }

    override fun reset() {
        weapons.clear()
    }
}