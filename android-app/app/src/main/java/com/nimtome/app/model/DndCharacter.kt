package com.nimtome.app.model

import com.nimtome.app.ui.theme.ColorPalette

data class DndCharacter(
    val id: Int = 0,
    val name: String = "",
    val level: Int = 0,
    val dndClass: DndClass = DndClass.None,
    val preferredColorPalette: ColorPalette = ColorPalette.Purple
) {
    override fun toString(): String {
        return "$name, level:$level, class:${dndClass.legibleName}"
    }
}
