package com.nimtome.app.functions.home

import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.theme.ColorPalette

data class HomeScreenState(
    val characters: List<DndCharacter> = emptyList(),
    val spells: List<Spell> = emptyList(),
    val colorPalette: ColorPalette = ColorPalette.Purple,
    val selectedList: HomeScreenSelection = HomeScreenSelection.CHARACTERS,
    val showDialog: Boolean = false,
    val editMode: Boolean = false,
)

enum class HomeScreenSelection(val legibleName: String) {
    CHARACTERS("Characters"),
    SPELLS("Spells"), ;
}
