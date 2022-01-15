package com.nimtome.app

import android.app.Application
import android.content.SharedPreferences
import androidx.room.Room
import com.nimtome.app.database.NimtomeDatabase
import com.nimtome.app.ui.theme.ColorPalette

class DndApplication : Application() {

    companion object {
        lateinit var nimtomeDatabase: NimtomeDatabase
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
        nimtomeDatabase = Room.databaseBuilder(
            applicationContext,
            NimtomeDatabase::class.java,
            "nimtome_database"
        ).build()

        sharedPreferences = getSharedPreferences(SP_ID_MAIN, MODE_PRIVATE)
        super.onCreate()
    }
}
