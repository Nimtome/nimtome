package com.nimtome.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.repository.CharacterRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repo = CharacterRepository(
        nimtomeDao = DndApplication.nimtomeDatabase.nimtomeDao()
    )

    val allCharacters: LiveData<List<DndCharacter>> = repo.getAllCharacters()

    fun insert(character: DndCharacter) = viewModelScope.launch {
        repo.insert(character)
    }

    fun delete(character: DndCharacter) = viewModelScope.launch {
        repo.delete(character)
    }

    fun get(id: Int): LiveData<DndCharacter> {
        var characterLiveData: LiveData<DndCharacter> = MediatorLiveData()
        viewModelScope.launch {
            characterLiveData = repo.get(id)
        }
        return characterLiveData
    }

    fun update(character: DndCharacter) = viewModelScope.launch {
        repo.update(character)
    }
}
