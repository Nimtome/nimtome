package com.nimtome.app.model

data class DndCharacter(
    val id: Int = 0,
    val name: String,
    val level: Int,
    val dndClass: DndClass,
    val spellList: List<String>,

    ) {

    constructor() : this(name = "", level = 0, dndClass = DndClass.None, spellList = listOf())
    constructor(name: String, level: Int, dndClass: DndClass) : this(
        name = name,
        level = level,
        dndClass = dndClass,
        spellList = listOf()
    )

    override fun toString(): String {
        return "${name}, level:${level}, class:${dndClass.legibleName}"
    }
}
