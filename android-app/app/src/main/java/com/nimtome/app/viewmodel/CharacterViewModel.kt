package com.nimtome.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
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
