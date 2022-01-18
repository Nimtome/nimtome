package com.nimtome.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nimtome.app.database.NimtomeDao
import com.nimtome.app.database.RoomCharacter
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(private val nimtomeDao: NimtomeDao) {

    fun getAllCharacters(): LiveData<List<DndCharacter>> {
        return nimtomeDao.getAllCharacters().map {
            it.map { roomCharacter ->
                roomCharacter.toDomainModel()
            }
        }
    }

    suspend fun insert(character: DndCharacter) = withContext(Dispatchers.IO) {
        nimtomeDao.addCharacter(character.toRoomDomain())
    }

    suspend fun delete(character: DndCharacter) = withContext(Dispatchers.IO) {
        val roomCharacter = nimtomeDao.getCharacter(character.id)
        roomCharacter?.let { nimtomeDao.deleteCharacter(roomCharacter) }
    }

    fun get(id: Int): LiveData<DndCharacter> {
        return nimtomeDao.getCharacterLiveData(id).map {
            it.toDomainModel()
        }
    }

    suspend fun update(character: DndCharacter) = withContext(Dispatchers.IO) {
        nimtomeDao.updateCharacter(character.toRoomDomain())
    }
}

fun DndCharacter.toRoomDomain(): RoomCharacter {
    return RoomCharacter(
        name = name,
        level = level,
        dndClass = dndClass.name,
        characterId = id
    )
}

private fun RoomCharacter.toDomainModel(): DndCharacter {
    return DndCharacter(
        name = name,
        level = level,
        dndClass = DndClass.valueOf(dndClass),
        id = characterId,
    )
}
