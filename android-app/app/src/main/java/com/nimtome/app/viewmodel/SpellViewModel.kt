package com.nimtome.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.DndApplication
import com.nimtome.app.model.Spell
import com.nimtome.app.repository.SpellRepository
import kotlinx.coroutines.launch

class SpellViewModel : ViewModel() {
    private val repo = SpellRepository(DndApplication.nimtomeDatabase.nimtomeDao())

//    val allSpells: LiveData<List<Spell>> = repo.getAllSpells()

    fun insert(spell: Spell) = viewModelScope.launch {
        repo.insert(spell)
    }

    fun delete(spell: Spell) = viewModelScope.launch {
        repo.delete(spell)
    }

    fun nuke() = viewModelScope.launch {
        repo.nuke()
    }

    fun get(spellId: Int): LiveData<Spell> {
        return repo.getSpell(spellId)
    }
}
