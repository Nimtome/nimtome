package com.nimtome.app.model

import com.nimtome.app.ui.theme.ColorPalette

data class DndCharacter(
    val id: Int = 0,
    val name: String,
    val level: Int,
    val dndClass: DndClass,
    val spellList: List<String>,
    val preferredColorPalette: ColorPalette = ColorPalette.Purple
) {

    constructor() : this(name = "", level = 0, dndClass = DndClass.None, spellList = listOf())

    override fun toString(): String {
        return "$name, level:$level, class:${dndClass.legibleName}"
    }
}
