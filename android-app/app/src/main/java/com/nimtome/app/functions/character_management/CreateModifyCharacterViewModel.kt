package com.nimtome.app.functions.character_management

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.repository.CharacterRepository
import com.nimtome.app.ui.logic.MAX_CHARACTER_LEVEL
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

    var event: MutableLiveData<CreateModifyCharacterEvent?> = MutableLiveData(null)

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
        val errors = validateCharacter(character)
        if (errors.isEmpty())
            event.value = CreateModifyCharacterEvent.CharacterOk
        else
            event.value = CreateModifyCharacterEvent.CharacterValidationFailed(errors)
    }

    fun submitCharacter() {
        if (validateCharacter(state.value.character).isEmpty()) {
            if (hadBase) {
                viewModelScope.launch {
                    characterRepository.update(_state.value.character)
                }
            } else {
                viewModelScope.launch {
                    characterRepository.insert(_state.value.character)
                }
            }

            event.value = CreateModifyCharacterEvent.FinishActivity
        }
    }

    private fun validateCharacter(dndCharacter: DndCharacter): List<CharacterValidationError> {
        val errors = mutableListOf<CharacterValidationError>()

        if (dndCharacter.name.isEmpty())
            errors.add(CharacterValidationError.WRONG_NAME)
        if (dndCharacter.dndClass == DndClass.None)
            errors.add(CharacterValidationError.WRONG_CLASS)
        if (dndCharacter.level > MAX_CHARACTER_LEVEL || dndCharacter.level < 0)
            errors.add(CharacterValidationError.WRONG_LEVEL)

        return errors
    }

    fun deleteCharacter() = viewModelScope.launch {
        characterRepository.delete(state.value.character)
        event.value = CreateModifyCharacterEvent.FinishActivity
    }
}