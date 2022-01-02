package com.nimtome.app.viewmodel.mock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nimtome.app.model.SpellSource
import com.nimtome.app.viewmodel.ISpellSourceViewModel

val SAMPLE_SPELL_SOURCES: List<SpellSource> = listOf(
    SpellSource("Test Spell source", "-", "1234", false)
)

class MockSpellSourceViewModel : ISpellSourceViewModel {
    override val lastError: LiveData<String?>
        get() = MutableLiveData<String>(null)

    override val spellSources: LiveData<List<SpellSource>?>
        get() = MutableLiveData(SAMPLE_SPELL_SOURCES)

    override fun fetchSources() {
    }

}