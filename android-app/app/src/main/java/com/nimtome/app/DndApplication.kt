package com.nimtome.app

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.nimtome.app.database.CharacterDatabase
import com.nimtome.app.database.SpellDatabase
import com.nimtome.app.ui.theme.ColorPalette

class DndApplication : Application() {

    companion object {
        lateinit var characterDatabase: CharacterDatabase
            private set
        lateinit var spellDatabase: SpellDatabase
            private set
        lateinit var sharedPreferences: SharedPreferences
            private set
        private const val SP_ID_MAIN = "com.nimtome.nimtome.main"
        private const val SP_COLOR_THEME = "COLOR_THEME"

        var colorPalette: ColorPalette
            get() {
                return ColorPalette.valueOf(
                    sharedPreferences
                        .getString(SP_COLOR_THEME, ColorPalette.Purple.name)
                        ?: ColorPalette.Purple.name
                )
            }
            set(value) {
                with(sharedPreferences.edit()) {
                    putString(SP_COLOR_THEME, value.name)
                    apply()
                }
            }
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

        sharedPreferences = getSharedPreferences(SP_ID_MAIN, MODE_PRIVATE)
        super.onCreate()
    }
}
