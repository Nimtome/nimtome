package com.nimtome.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [RoomCharacter::class]
)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}
