package com.flyinpancake.dndspells.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.flyinpancake.dndspells.database.RoomSpell
import com.flyinpancake.dndspells.database.SpellDao
import com.flyinpancake.dndspells.model.Spell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpellRepository(private val spellDao: SpellDao) {
    fun getAllSpells(): LiveData<List<Spell>> {
        return spellDao.getAllSpells().map { roomSpells ->
            roomSpells.map { roomSpell -> roomSpell.toDomainModel() }
        }
    }

    suspend fun insert(spell: Spell) = withContext(Dispatchers.IO) {
        if (spellDao.getSpellByName(spell.name) == null)
            spellDao.insertSpell(spell.toRoomModel())
    }

    suspend fun delete(spell: Spell) = withContext(Dispatchers.IO) {
        val roomSpell = spellDao.getSpellByName(spell.name)?: return@withContext
        spellDao.deleteSpell(roomSpell)
    }

    suspend fun nuke() = withContext(Dispatchers.IO) {
        spellDao.nukeSpells()
    }

    fun getSpellByName(spellName: String): LiveData<Spell> {
        return spellDao.getSpellLiveByName(spellName).map { it.toDomainModel() }
    }


}

private fun Spell.toRoomModel(): RoomSpell {
    return RoomSpell(
        name = name,
        desc = desc,
        classes = classes,
        components = components,
        duration = duration,
        level = level,
        range = range,
        ritual = ritual,
        school = school,
        time = time,
        roll = roll
    )
}


fun RoomSpell.toDomainModel(): Spell {
    return Spell(name, desc, level, components, range, time, school, ritual, duration, classes, roll)
}


