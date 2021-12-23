package com.flyinpancake.dndspells.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.flyinpancake.dndspells.database.CharacterDao
import com.flyinpancake.dndspells.database.RoomCharacter
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.model.DndClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepository(
    private val characterDao: CharacterDao,
) {
    fun getAllCharacters(): LiveData<List<DndCharacter>> {
        return characterDao.getAllCharacters().map {
            it.map { roomCharacter -> roomCharacter.toDomainModel() }
        }
    }

    suspend fun insert(character: DndCharacter) = withContext(Dispatchers.IO) {
        if (characterDao.getCharacterByName(character.name) == null)
            characterDao.addCharacter(character.toRoomDomain())
    }

    suspend fun delete(character: DndCharacter) = withContext(Dispatchers.IO) {
        val roomCharacter = characterDao.getCharacter(character.id)
        roomCharacter?.let { characterDao.deleteCharacter(roomCharacter) }
    }

    fun get(name: String): LiveData<DndCharacter> {
        return characterDao.getCharacterLiveDataByName(name).map { it.toDomainModel() }
    }

    suspend fun update(character: DndCharacter) = withContext(Dispatchers.IO) {
        val roomCharacter = character.toRoomDomain()
        characterDao.updateCharacter(roomCharacter)
    }
}

private fun DndCharacter.toRoomDomain(): RoomCharacter {
    var spellNameList = ""
    spellList.forEach {
        spellNameList += "\n"
        spellNameList += it
    }
    return RoomCharacter(
        name = name,
        level = level,
        dndClass = dndClass.legibleName,
        spellNameList = spellNameList,
        id = id
    )

}

private fun RoomCharacter.toDomainModel(): DndCharacter {
    var characterClass: DndClass = DndClass.None
    DndClass.values().forEach {
        if (it.legibleName == dndClass){
            characterClass = it
        }
    }
    return DndCharacter(
        name = name,
        level = level,
        id = id,
        spellList = spellNameList.split('\n'),
        dndClass = characterClass
    )
}
