package com.nimtome.app.functions.character_management

sealed class CreateModifyCharacterEvent {
    object FinishActivity : CreateModifyCharacterEvent()
    data class CharacterValidationFailed(val errors : List<CharacterValidationError>) : CreateModifyCharacterEvent()
    object CharacterOk : CreateModifyCharacterEvent()
}

enum class CharacterValidationError {
    WRONG_NAME,
    WRONG_CLASS,
    WRONG_LEVEL,
}
