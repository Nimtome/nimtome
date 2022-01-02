package com.nimtome.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.Spell
import com.nimtome.app.repository.SpellRepository
import kotlinx.coroutines.launch

class SpellViewModel : ViewModel() {
    private val repo: SpellRepository

    val allSpells: LiveData<List<Spell>>

    init {
        val spellDao = DndApplication.spellDatabase.spellDao()
        repo = SpellRepository(spellDao)
        allSpells = repo.getAllSpells()
    }

    fun insert(spell: Spell) = viewModelScope.launch {
        repo.insert(spell)
    }

    fun delete(spell: Spell) = viewModelScope.launch {
        repo.delete(spell)
    }

    fun nuke() = viewModelScope.launch {
        repo.nuke()
    }

    fun get(spellName: String): LiveData<Spell> {
        return repo.getSpellByName(spellName)
    }
}
