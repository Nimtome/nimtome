package com.nimtome.app.functions.character_management

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.repository.CharacterRepository
import com.nimtome.app.ui.logic.validateCharacter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateModifyCharacterViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    private val _state = mutableStateOf(CreateModifyCharacterState())
    val state: State<CreateModifyCharacterState> = _state

    private var hadBase = false

    fun submitBaseCharacterId(characterId: Int) = viewModelScope.launch {
        characterRepository.getFlow(characterId).collect {
            _state.value = _state.value.copy(character = it ?: DndCharacter())
            it?.let {
                if (it.id != 0)
                    hadBase = true
            }
        }
    }

    fun updateCharacter(character: DndCharacter) {
        _state.value = _state.value.copy(character = character)
    }

    fun submitCharacter() {
        if (validateCharacter(state.value.character)) {
            if (hadBase) {
                viewModelScope.launch {
                    characterRepository.update(_state.value.character)
                }
            } else {
                viewModelScope.launch {
                    characterRepository.insert(_state.value.character)
                }
            }
        } else {
            //TODO signal to view to highlight error
        }
    }

    fun deleteCharacter() = viewModelScope.launch {
        characterRepository.delete(state.value.character)
    }
}