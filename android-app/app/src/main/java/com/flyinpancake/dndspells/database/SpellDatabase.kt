package com.flyinpancake.dndspells.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [RoomSpell::class]
)
abstract class SpellDatabase: RoomDatabase() {
    abstract fun spellDao(): SpellDao
}