package com.nimtome.app.ui.components

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.nimtome.app.R
import com.nimtome.app.viewmodel.SpellViewModel

@ExperimentalMaterialApi
@Composable
private fun DownloadSpellsButton(
    spellsViewModel: SpellViewModel,
) {
    IconButton(onClick = {importSpells()}) {
        Icon(painterResource(id = R.drawable.plus), "Download spells")
    }
}