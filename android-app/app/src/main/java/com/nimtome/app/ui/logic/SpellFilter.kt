package com.nimtome.app.ui.logic

import androidx.core.util.toRange
import com.nimtome.app.model.DndClass
import com.nimtome.app.model.Spell

data class SpellFilter(
    val nameFilter: String = "",
    val levelFilter: ClosedFloatingPointRange<Float> = 0f..MAX_SPELL_LEVEL.toFloat(),
    val levelFilterEnabled: Boolean = false,
    val componentFilter: String = "",
    val componentFilterEnabled: Boolean = false,
    val classFilter: DndClass,
    val classFilterEnabled: Boolean = false,
) {
    fun filterSpells(spells: List<Spell>): List<Spell> {
        return spells.filter { spell ->
            nameFilter.isBlank() || spell.name.contains(nameFilter.trim(), ignoreCase = true)
        }.filter { spell ->
            !levelFilterEnabled || levelFilter.toRange().contains(spell.level.toFloat())
        }.filter { spell ->
            !componentFilterEnabled || componentFilter.all { char -> spell.components.contains(char) }
        }.filter { spell ->
            !classFilterEnabled || spell.classes.contains(classFilter.legibleName.split(" ")[0])
        }
    }
}
