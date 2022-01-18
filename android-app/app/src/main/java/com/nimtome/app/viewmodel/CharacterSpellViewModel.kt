package com.nimtome.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.repository.CharacterSpellRepository
import kotlinx.coroutines.launch

class CharacterSpellViewModel : ViewModel() {
    private val repo = CharacterSpellRepository(
        DndApplication.nimtomeDatabase.nimtomeDao()
    )

    fun getSpellsForCharacter(characterId: Int): LiveData<List<Spell>> {
        return repo.getLiveCharacterSpellList(characterId)
    }

    fun submitSpellist(character: DndCharacter, spells: List<Spell>) {
        viewModelScope.launch {
            repo.submitSpellList(character, spells)
        }
    }

}
