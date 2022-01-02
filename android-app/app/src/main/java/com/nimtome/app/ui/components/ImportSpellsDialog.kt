package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nimtome.app.model.SpellSource
import com.nimtome.app.viewmodel.ISpellSourceViewModel

const val IMPORT_FROM_FILE_DESC: String =
    "You will have to select a correct xml file from your device"

const val IMPORT_FROM_FILE_ID: String =
    "file"


@Composable
private fun SpellSourceRadioButtonItem(
    spellSource: SpellSource,
) {
    Text(
        text = spellSource.name,
        style = MaterialTheme.typography.body1,
    )
}

@Composable
private fun SpellSourceRadioButton(
    selectedSourceId: String?,
    sources: List<SpellSource>,
    onSourceSelected: (source: SpellSource) -> Unit,
) {
    Column {
        sources.forEach { source ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (source.gistId == selectedSourceId),
                        onClick = {
                            onSourceSelected(source)
                        }
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                RadioButton(
                    selected = (source.gistId == selectedSourceId),
                    onClick = { onSourceSelected(source) }
                )
                SpellSourceRadioButtonItem(source)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun ImportSpellsDialog(
    onCloseRequest: () -> Unit,
    spellSourceViewModel: ISpellSourceViewModel,
    onImportSpells: (spellSource: SpellSource) -> Unit,
) {
    val downloadableSpellSources = spellSourceViewModel.spellSources.observeAsState().value
    val allSpellSources = arrayListOf<SpellSource>().let {
        if (downloadableSpellSources != null) {
            it.addAll(downloadableSpellSources)
        }

        it.add(SpellSource("Import from file", IMPORT_FROM_FILE_DESC, IMPORT_FROM_FILE_ID, true))

        return@let it
    }

    val selectedSourceId = remember { mutableStateOf(allSpellSources[0].gistId) }
    val lastErrorState = spellSourceViewModel.lastError.observeAsState()

    AlertDialog(
        onDismissRequest = {
            onCloseRequest()
        },
        title = {
            Text(text = "Import spells")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(0.dp),
            ) {
                if (lastErrorState.value != null) {
                    Text(
                        text = lastErrorState.value.orEmpty(),
                        style = MaterialTheme.typography.body1,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 5.dp),
                    )
                }

                SpellSourceRadioButton(
                    selectedSourceId = selectedSourceId.value,
                    onSourceSelected = { selectedSourceId.value = it.gistId },
                    sources = allSpellSources
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(bottom = 5.dp, end = 5.dp),
                onClick = {
                    val selectedSource =
                        allSpellSources.find { it.gistId == selectedSourceId.value }
                    if (selectedSource != null) {
                        onImportSpells(selectedSource)
                    }
                }) {
                Text("Ok")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(bottom = 5.dp),
                onClick = {
                    onCloseRequest()
                }) {
                Text("Cancel")
            }
        }
    )
}
