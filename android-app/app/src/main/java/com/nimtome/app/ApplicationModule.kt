package com.nimtome.app

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.nimtome.app.database.NimtomeDatabase
import com.nimtome.app.repository.CharacterRepository
import com.nimtome.app.repository.SpellRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    private const val DATABASE_NAME = "nimtome_database"
    private const val SP_ID_MAIN = "com.nimtome.nimtome.main"

    @Singleton
    @Provides
    fun provideNimtomeDatabase(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        NimtomeDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCharacterRepository(db: NimtomeDatabase): CharacterRepository {
        return CharacterRepository(db.nimtomeDao())
    }

    @Singleton
    @Provides
    fun provideSpellRepository(db: NimtomeDatabase): SpellRepository {
        return SpellRepository(db.nimtomeDao())
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(
            SP_ID_MAIN, MODE_PRIVATE
        )
}