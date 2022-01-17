package com.nimtome.app.model

data class DndCharacter(
    val id: Int = 0,
    val name: String = "",
    val level: Int = 0,
    val dndClass: DndClass = DndClass.None,
) {
    override fun toString(): String {
        return "$name, level:$level, class:${dndClass.legibleName}"
    }
}
