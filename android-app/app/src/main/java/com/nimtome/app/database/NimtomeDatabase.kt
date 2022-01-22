package com.nimtome.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 2,
    exportSchema = true,
    entities = [
        RoomCharacter::class,
        RoomSpell::class,
        CharacterSpellCrossRef::class
    ],
    autoMigrations = []
)
abstract class NimtomeDatabase : RoomDatabase() {
    abstract fun nimtomeDao(): NimtomeDao
}
