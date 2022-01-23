package com.nimtome.app.functions.home

import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell

sealed class HomeViewEvent {
    object AddCharacterEvent : HomeViewEvent()
    data class ModifyCharacter(val character: DndCharacter) : HomeViewEvent()
    object ImportSpellsEvent : HomeViewEvent()
    data class OpenCharacterEvent(val character: DndCharacter) : HomeViewEvent()
    data class OpenSpellEvent(val spell: Spell) : HomeViewEvent()
    object OpenSpellImporter : HomeViewEvent()
}