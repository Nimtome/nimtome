package com.nimtome.app.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SpellDao {

    @Insert
    fun insertSpell(spell: RoomSpell)

    @Query("SELECT * FROM spells")
    fun getAllSpells(): LiveData<List<RoomSpell>>

    @Query("SELECT * FROM spells WHERE id == :id")
    fun getSpellById(id: Int): RoomSpell?

    @Query("SELECT * FROM spells WHERE name == :name")
    fun getSpellByName(name: String): RoomSpell?

    @Query("SELECT * FROM spells WHERE name == :name")
    fun getSpellLiveByName(name: String): LiveData<RoomSpell>

    @Delete
    fun deleteSpell(spell: RoomSpell)

    @Query("DELETE FROM spells")
    fun nukeSpells()
}
