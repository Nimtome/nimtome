package com.nimtome.app

import android.app.Application
import androidx.room.Room
import com.nimtome.app.database.CharacterDatabase
import com.nimtome.app.database.SpellDatabase

class DndApplication : Application() {

    companion object {
        lateinit var characterDatabase: CharacterDatabase
            private set
        lateinit var spellDatabase: SpellDatabase
            private set
    }

    override fun onCreate() {
        spellDatabase = Room.databaseBuilder(
            applicationContext,
            SpellDatabase::class.java,
            "spell_database",
        ).fallbackToDestructiveMigration().build()

        characterDatabase = Room.databaseBuilder(
            applicationContext,
            CharacterDatabase::class.java,
            "character_database",
        ).fallbackToDestructiveMigration().build()
        super.onCreate()
    }
}
