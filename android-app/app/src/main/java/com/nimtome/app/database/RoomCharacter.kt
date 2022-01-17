package com.nimtome.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
class RoomCharacter(
    @PrimaryKey(autoGenerate = true)
    val characterId: Int = 0,
    val name: String,
    val level: Int,
    val dndClass: String,
)
