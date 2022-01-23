package com.nimtome.app.functions.home

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.repository.CharacterRepository
import com.nimtome.app.repository.SpellRepository
import com.nimtome.app.ui.theme.ColorPalette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val spellRepository: SpellRepository,
    sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    private var getCharactersJob: Job? = null
    private var getSpellsJob: Job? = null

    val viewEvent: MutableLiveData<HomeViewEvent?> = MutableLiveData(null)


    init {
        getCharactersJob = characterRepository.getAllCharacters()
            .onEach {
                _state.value = _state.value.copy(characters = it)
            }
            .launchIn(viewModelScope)

        getSpellsJob = spellRepository.getAllSpells()
            .onEach {
                _state.value = _state.value.copy(spells = it)
            }
            .launchIn(viewModelScope)

        _state.value =
            _state.value.copy(colorPalette = sharedPreferences.getString(
                DndApplication.SP_COLOR_THEME,
                ColorPalette.Purple.name
            )
                ?.let { ColorPalette.valueOf(it) } ?: ColorPalette.Purple)

    }

    fun submitNewSpellList(spellList: List<Spell>) = viewModelScope.launch {
        spellRepository.nuke()
        spellList.forEach {
            spellRepository.insert(it)
        }
    }

    fun changePreferredColors(colorPalette: ColorPalette) {
        _state.value = _state.value.copy(colorPalette = colorPalette)
    }

    fun selectContentList(selection: HomeScreenSelection) {
        _state.value = _state.value.copy(selectedList = selection)
    }

    fun importSpellsFromFile() {
        viewEvent.value = HomeViewEvent.OpenSpellImporter
    }

    fun openAddCharacter() {
        viewEvent.value = HomeViewEvent.AddCharacterEvent
    }

    fun openCharacterDetails(character: DndCharacter) {
        viewEvent.value = HomeViewEvent.OpenCharacterEvent(character)
    }

    fun modifyCharacter(character: DndCharacter) {
        viewEvent.value = HomeViewEvent.ModifyCharacter(character)
    }

    fun openSpellDetails(spell: Spell) {
        viewEvent.value = HomeViewEvent.OpenSpellEvent(spell)
    }

    fun changeShowDialog(showDialog: Boolean) {
        _state.value = _state.value.copy(showDialog = showDialog)
    }

    fun changeEditMode(editMode: Boolean) {
        _state.value = _state.value.copy(editMode = editMode)
    }
}
