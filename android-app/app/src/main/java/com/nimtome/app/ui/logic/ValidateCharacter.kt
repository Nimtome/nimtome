package com.nimtome.app.ui.logic

import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass

fun validateCharacter(dndCharacter: DndCharacter): Boolean {
    // General shit
    var valid = true
    if (dndCharacter.name.isEmpty())
        valid = false
    if (dndCharacter.dndClass == DndClass.None)
        valid = false
    if (dndCharacter.level > MAX_CHARACTER_LEVEL || dndCharacter.level < 0)
        valid = false

    return valid
}
