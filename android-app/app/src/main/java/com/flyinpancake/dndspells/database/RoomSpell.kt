package com.flyinpancake.dndspells.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "spells", indices = [Index(value = ["name"], unique = true)])
data class RoomSpell(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val desc: String,
    val level: Int,
    val components: String,
    val range: String,
    val time: String,
    val school: String,
    val ritual: Boolean,
    val duration: String,
    val classes: String,
    val roll: String?
)