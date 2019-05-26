package com.spacehorde.entities

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.MathUtils
import com.spacehorde.Groups
import com.spacehorde.components.GroupMask
import com.spacehorde.components.Weaponized
import com.spacehorde.components.component
import com.spacehorde.ships.PlayerShipMask
import com.spacehorde.weapons.TwinBullet

class PlayerShipGenerator : ShipGenerator() {
    override fun generate(engine: Engine): Entity {
        val entity = createShip(PlayerShipMask(), MathUtils.random() * 360f)
        entity.add(component<GroupMask> { mask = Groups.PLAYERS })
        entity.add(component<Weaponized> { add(TwinBullet()) })

        return entity
    }
}