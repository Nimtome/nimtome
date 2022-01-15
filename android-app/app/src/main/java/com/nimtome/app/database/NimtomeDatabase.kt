package com.nimtome.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = true,
    entities = [RoomCharacter::class, RoomSpell::class],
    autoMigrations = []
)
abstract class NimtomeDatabase : RoomDatabase() {
    abstract fun spellDao(): SpellDao
    abstract fun characterDao(): CharacterDao
}
