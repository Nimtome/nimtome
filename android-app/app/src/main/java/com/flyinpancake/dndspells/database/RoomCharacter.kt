package com.flyinpancake.dndspells.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
class RoomCharacter(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val level: Int,
    val dndClass: String,
    val spellNameList: String,
)