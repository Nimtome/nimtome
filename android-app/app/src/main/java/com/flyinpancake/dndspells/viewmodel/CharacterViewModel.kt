package com.flyinpancake.dndspells.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flyinpancake.dndspells.DndApplication
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterViewModel: ViewModel() {
    private val repo: CharacterRepository

    val allCharacters: LiveData<List<DndCharacter>>

    init {
        val characterDao = DndApplication.characterDatabase.characterDao()
        repo = CharacterRepository(characterDao)
        allCharacters = repo.getAllCharacters()
    }

    fun insert(character: DndCharacter) = viewModelScope.launch {
        repo.insert(character)
    }

    fun delete(character: DndCharacter) = viewModelScope.launch {
        repo.delete(character)
    }

    fun get(name: String): LiveData<DndCharacter> {
        return repo.get(name)
    }

    fun update(character: DndCharacter) = viewModelScope.launch {
        repo.update(character)
    }
}
