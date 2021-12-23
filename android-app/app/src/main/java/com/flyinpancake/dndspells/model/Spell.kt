package com.flyinpancake.dndspells.model

import kotlinx.serialization.Serializable

@Serializable
data class Spell (
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
) {
    constructor() : this(
        name = "",
        desc = "",
        level = 0,
        components = "",
        range = "",
        time = "",
        school = "",
        ritual = false,
        duration = "",
        classes = "",
        roll = null
    )
}
