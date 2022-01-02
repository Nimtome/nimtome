package com.nimtome.app.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nimtome.app.BuildConfig
import com.nimtome.app.DndApplication
import com.nimtome.app.model.SpellSource
import com.nimtome.app.repository.SpellRepository
import com.nimtome.app.services.spellSourceGistService.SpellSourceGistService
import kotlinx.coroutines.launch

interface ISpellSourceViewModel {
    val lastError: LiveData<String?>

    val spellSources: LiveData<List<SpellSource>?>

    fun fetchSources()
}

class SpellSourceViewModel : ViewModel(), ISpellSourceViewModel {
    private val spellRepository: SpellRepository

    private val spellSourcesService: SpellSourceGistService

    private val _spellSources = MutableLiveData<List<SpellSource>?>(null)

    private val _lastError = MutableLiveData<String?>(null)

    override val lastError: LiveData<String?>
        get() = _lastError

    override val spellSources: LiveData<List<SpellSource>?>
        get() = _spellSources


    init {
        val spellDao = DndApplication.spellDatabase.spellDao()
        this.spellRepository = SpellRepository(spellDao)
        this.spellSourcesService = SpellSourceGistService()
    }

    override fun fetchSources() {
        Log.d("Nimtome", "called fetch")

        viewModelScope.launch {
            spellSourcesService.fetchSources(
                BuildConfig.GISTS_USER,
                BuildConfig.SPELL_SOURCES_GIST_ID
            ) {
                if (it == null) {
                    _lastError.value = "Couldn't download sources"
                } else {
                    Log.d("Nimtome", "sources here ${it.list.size}")
                    _spellSources.value = it.list.map { source ->
                        return@map source.toSpellSource()
                    }
                }
            }
        }
    }
}
