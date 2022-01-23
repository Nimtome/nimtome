package com.nimtome.app.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.functions.presentation.sampleCharacter
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.ui.theme.CARD_ELEVATION
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import com.nimtome.app.ui.theme.DndSpellsTheme

@ExperimentalMaterialApi
@Composable
fun CharacterCard(
    character: DndCharacter,
    onClick: (DndCharacter) -> Unit,
    editMode: Boolean = false,
    onEditClick: (DndCharacter) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(character) },
        modifier = modifier,
        elevation = CARD_ELEVATION
    ) {
        Row(Modifier.fillMaxWidth()) {
            AnimatedVisibility(
                visible = editMode,
                enter = fadeIn() + slideInHorizontally(),
                exit = fadeOut() + slideOutHorizontally()
            ) {
                IconButton(onClick = { onEditClick(character) }) {
                    Icon(Icons.Default.Edit, "Edit Character")
                }
            }


            CharacterContent(
                character = character,
            )
        }
    }
}

@Composable
private fun CharacterContent(
    character: DndCharacter,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Column(
        modifier
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                character.name,
                style = MaterialTheme.typography.body1
            )

            Text(
                "Level ${character.level} ${character.dndClass.legibleName}",
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@ExperimentalMaterialApi
@Composable
@Preview
private fun CharacterCardPreview() {

    DndSpellsTheme {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterCard(
                character = sampleCharacter,
                onClick = {},
                modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO)
            )
        }
    }
}

@Suppress("UnusedPrivateMember")
@ExperimentalMaterialApi
@Composable
@Preview
private fun EditCharacterCardPreview() {

    DndSpellsTheme {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterCard(
                character = sampleCharacter,
                onClick = {},
                editMode = true,
                onEditClick = {},
                modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO)
            )
        }
    }
}
