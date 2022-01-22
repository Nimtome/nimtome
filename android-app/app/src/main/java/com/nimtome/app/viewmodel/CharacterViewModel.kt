package com.nimtome.app.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.nimtome.app.DndApplication
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.repository.CharacterRepository
import com.nimtome.app.repository.CharacterSpellRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

class CharacterViewModel : ViewModel() {

    private val _activeCharacter: MutableLiveData<DndCharacter?> = MutableLiveData(null)

    val activeCharacter: LiveData<DndCharacter?> get() = _activeCharacter

    private val repo = CharacterRepository(
        nimtomeDao = DndApplication.nimtomeDatabase.nimtomeDao()
    )

    val allCharacters: LiveData<List<DndCharacter>> = repo.getAllCharacters()

    fun insert(character: DndCharacter) = viewModelScope.launch {
        repo.insert(character)
    }

    fun delete(character: DndCharacter) = viewModelScope.launch {
        CharacterSpellRepository(DndApplication.nimtomeDatabase.nimtomeDao()).submitSpellList(
            character,
            listOf()
        )
        repo.delete(character)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setActiveCharacterById(id: Int) = viewModelScope.launch {
        val channel = Channel<DndCharacter?>()
        GlobalScope.launch {
            channel.send(repo.getById(id))
        }
        _activeCharacter.value = channel.receive()
    }

    fun update(character: DndCharacter) = viewModelScope.launch {
        repo.update(character)
    }
}
