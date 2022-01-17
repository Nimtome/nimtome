package com.nimtome.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nimtome.app.database.CharacterSpellCrossRef
import com.nimtome.app.database.CharacterWithSpells
import com.nimtome.app.database.NimtomeDao
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterSpellRepository(private val nimtomeDao: NimtomeDao) {
    fun getLiveCharacterSpellList(characterId: Int): LiveData<List<Spell>> {
        return nimtomeDao.getLiveSpellsForCharacter(characterId)
            .map { it.spells.map { roomSpell -> roomSpell.toDomainModel() } }
    }

    suspend fun submitSpellList(character: DndCharacter, spells: List<Spell>) = withContext(Dispatchers.IO) {
        val oldList = nimtomeDao.getSpellsForCharacter(character.id).spells

        // Removed
        oldList.filter { !spells.map { spell -> spell.toRoomModel() }.contains(it) }.forEach { spell ->
            nimtomeDao.removeCharacterSpell(CharacterSpellCrossRef(character.id, spell.id))
        }

        spells.map { it.toRoomModel() }.filter { !oldList.contains(it) }.forEach {
            nimtomeDao.addCharacterSpell(CharacterSpellCrossRef(character.id, it.id))
        }
    }
}
