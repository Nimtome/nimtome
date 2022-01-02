package com.nimtome.app.model

data class SpellSource(
    val name: String,
    val description: String,
    val gistId: String?,

    val isFromFile: Boolean
)