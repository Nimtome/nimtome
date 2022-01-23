package com.nimtome.app.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.nimtome.app.database.NimtomeDao
import com.nimtome.app.database.RoomCharacter
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.ui.theme.ColorPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class CharacterRepository(private val nimtomeDao: NimtomeDao) {

    fun getAllCharacters(): Flow<List<DndCharacter>> {
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

    fun getLive(id: Int): LiveData<DndCharacter> {
        return nimtomeDao.getCharacterLiveData(id).map {
            it.toDomainModel()
        }
    }

    fun getById(id: Int): DndCharacter? {
        return nimtomeDao.getCharacter(id)?.toDomainModel()
    }

    suspend fun update(character: DndCharacter) = withContext(Dispatchers.IO) {
        nimtomeDao.updateCharacter(character.toRoomDomain())
    }

    suspend fun getFlow(id: Int): Flow<DndCharacter?> {
        return nimtomeDao.getCharacterFlow(id).map { it?.toDomainModel() }
    }
}

fun DndCharacter.toRoomDomain(): RoomCharacter {
    return RoomCharacter(
        name = name,
        level = level,
        dndClass = dndClass.name,
        characterId = id,
        preferredColorPaletteName = preferredColorPalette.name
    )
}

private fun RoomCharacter.toDomainModel(): DndCharacter {
    return DndCharacter(
        name = name,
        level = level,
        dndClass = DndClass.valueOf(dndClass),
        id = characterId,
        preferredColorPalette = ColorPalette.valueOf(preferredColorPaletteName)
    )
}
