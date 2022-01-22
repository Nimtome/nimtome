package com.nimtome.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nimtome.app.database.NimtomeDao
import com.nimtome.app.database.RoomSpell
import com.nimtome.app.model.Spell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SpellRepository(private val nimtomeDao: NimtomeDao) {
    fun getAllSpells(): LiveData<List<Spell>> {
        return nimtomeDao.getAllSpells().map {
            it.map { roomSpell ->
                roomSpell.toDomainModel()
            }
        }
    }

    suspend fun insert(spell: Spell) = withContext(Dispatchers.IO) {
        nimtomeDao.insertSpell(spell.toRoomModel())
    }

    suspend fun delete(spell: Spell) = withContext(Dispatchers.IO) {
        nimtomeDao.getSpellById(spell.id) ?: return@withContext
        nimtomeDao.deleteSpell(spell.toRoomModel())
    }

    suspend fun nuke() = withContext(Dispatchers.IO) {
        nimtomeDao.nukeSpells()
    }

    fun getSpell(spellId: Int): LiveData<Spell> {
        return nimtomeDao.getSpellLiveById(spellId).map { it.toDomainModel() }
    }
}

fun Spell.toRoomModel(): RoomSpell {
    return RoomSpell(
        id = id,
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
    return Spell(
        id,
        name,
        desc,
        level,
        components,
        range,
        time,
        school,
        ritual,
        duration,
        classes,
        roll
    )
}
