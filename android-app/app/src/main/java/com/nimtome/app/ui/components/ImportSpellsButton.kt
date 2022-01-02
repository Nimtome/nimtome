package com.nimtome.app.ui.components

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import com.nimtome.app.R
import com.nimtome.app.model.SpellSource
import com.nimtome.app.viewmodel.ISpellSourceViewModel

@ExperimentalMaterialApi
@Composable
fun ImportSpellsButton(
    spellSourceViewModel: ISpellSourceViewModel,
    onImportSpells: (spellSource: SpellSource) -> Unit,
) {
    val isDownloadSpellListModalOpen = remember { mutableStateOf(false) }

    IconButton(onClick = { isDownloadSpellListModalOpen.value = true }) {
        Icon(painterResource(id = R.drawable.application_import), "Import spells")
    }

    if (isDownloadSpellListModalOpen.value) {
        ImportSpellsDialog(
            spellSourceViewModel = spellSourceViewModel,
            onCloseRequest = { isDownloadSpellListModalOpen.value = false },
            onImportSpells = onImportSpells
        )
    }
}
