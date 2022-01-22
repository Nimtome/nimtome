package com.nimtome.app.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "characterSpells",
    primaryKeys = [
        "characterId",
        "spellId"
    ]
)
data class CharacterSpellCrossRef(
    val characterId: Int,
    val spellId: Int
)

data class CharacterWithSpells(
    @Embedded val character: RoomCharacter,
    @Relation(
        parentColumn = "characterId",
        entityColumn = "spellId",
        associateBy = Junction(CharacterSpellCrossRef::class)
    )
    val spells: List<RoomSpell>
)
