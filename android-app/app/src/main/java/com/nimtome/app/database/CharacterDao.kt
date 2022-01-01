package com.nimtome.app.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CharacterDao {
    @Insert
    fun addCharacter(character: RoomCharacter)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): LiveData<List<RoomCharacter>>

    @Query("SELECT * FROM characters WHERE id == :id")
    fun getCharacter(id: Int): RoomCharacter?

    @Query("SELECT * FROM characters WHERE name == :name")
    fun getCharacterByName(name: String): RoomCharacter?

    @Query("SELECT * FROM characters WHERE name == :name")
    fun getCharacterLiveDataByName(name: String): LiveData<RoomCharacter>

    @Update
    fun updateCharacter(vararg roomCharacters: RoomCharacter): Int

    @Delete
    fun deleteCharacter(
        character: RoomCharacter
    )
}
