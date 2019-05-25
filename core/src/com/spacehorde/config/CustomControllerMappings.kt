package com.spacehorde.config

import com.spacehorde.service.registerService
import de.golfgl.gdx.controllers.mapping.ConfiguredInput
import de.golfgl.gdx.controllers.mapping.ControllerMappings

class CustomControllerMappings : ControllerMappings() {
    companion object {
        const val BUTTON_ACCEPT = 0
        const val BUTTON_BOMB = 1
        const val BUTTON_START = 2
        const val BUTTON_CANCEL = 3
        const val MOVE_VERTICAL = 4
        const val MOVE_HORIZONTAL = 5
        const val FIRE_VERTICAL = 6
        const val FIRE_HORIZONTAL = 7
    }

    init {
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.button, BUTTON_ACCEPT))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.button, BUTTON_BOMB))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.button, BUTTON_START))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.button, BUTTON_CANCEL))

        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.axisAnalog, MOVE_VERTICAL))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.axisAnalog, MOVE_HORIZONTAL))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.axisAnalog, FIRE_VERTICAL))
        addConfiguredInput(ConfiguredInput(ConfiguredInput.Type.axisAnalog, FIRE_HORIZONTAL))

        commitConfig()

        registerService(this)
    }

    override fun getDefaultMapping(defaultMapping: MappedInputs): Boolean {
        defaultMapping.putMapping(MappedInput(BUTTON_ACCEPT, ControllerButton(0)))
        defaultMapping.putMapping(MappedInput(BUTTON_BOMB, ControllerButton(2)))
        defaultMapping.putMapping(MappedInput(BUTTON_START, ControllerButton(6)))
        defaultMapping.putMapping(MappedInput(BUTTON_CANCEL, ControllerButton(4)))

        defaultMapping.putMapping(MappedInput(MOVE_HORIZONTAL, ControllerAxis(0)))
        defaultMapping.putMapping(MappedInput(MOVE_VERTICAL, ControllerAxis(1)))
        defaultMapping.putMapping(MappedInput(FIRE_HORIZONTAL, ControllerAxis(2)))
        defaultMapping.putMapping(MappedInput(FIRE_VERTICAL, ControllerAxis(3)))

        return true
    }
}
