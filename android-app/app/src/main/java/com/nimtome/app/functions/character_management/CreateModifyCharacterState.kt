package com.nimtome.app.functions.character_management

import com.nimtome.app.model.DndCharacter
import com.nimtome.app.ui.theme.ColorPalette

data class CreateModifyCharacterState(
    val character: DndCharacter = DndCharacter(),
) {
    val preferredColorPalette: ColorPalette
        get() = character.preferredColorPalette
}
