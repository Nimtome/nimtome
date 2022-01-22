package com.nimtome.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NimtomeDao {
    @Insert
    fun addCharacter(character: RoomCharacter)

    @Query("SELECT * FROM characters")
    fun getAllCharacters(): LiveData<List<RoomCharacter>>

    @Query("SELECT * FROM characters WHERE characterId == :id")
    fun getCharacter(id: Int): RoomCharacter?

    @Query("SELECT * FROM characters WHERE characterId == :id")
    fun getCharacterLiveData(id: Int): LiveData<RoomCharacter>

    @Update
    fun updateCharacter(vararg roomCharacters: RoomCharacter): Int

    @Delete
    fun deleteCharacter(character: RoomCharacter)

    @Insert
    fun insertSpell(spell: RoomSpell)

    @Query("SELECT * FROM spells")
    fun getAllSpells(): LiveData<List<RoomSpell>>

    @Query("SELECT * FROM spells WHERE spellId == :id")
    fun getSpellById(id: Int): RoomSpell?

    @Query("SELECT * FROM spells WHERE spellId == :spellId")
    fun getSpellLiveById(spellId: Int): LiveData<RoomSpell>

    @Delete
    fun deleteSpell(spell: RoomSpell)

    @Query("DELETE FROM spells")
    fun nukeSpells()

    @Transaction
    @Query("SELECT * FROM characters WHERE characterId == :id")
    fun getSpellsForCharacter(id: Int): CharacterWithSpells

    @Transaction
    @Query("SELECT * FROM characters WHERE characterId == :id")
    fun getLiveSpellsForCharacter(id: Int): LiveData<CharacterWithSpells>

    @Insert
    fun addCharacterSpell(characterSpell: CharacterSpellCrossRef)

    @Delete
    fun removeCharacterSpell(characterSpell: CharacterSpellCrossRef)
}
