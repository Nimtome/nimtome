package com.flyinpancake.dndspells.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.sampleCharacter
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme
import com.flyinpancake.dndspells.ui.theme.CardElevation


@ExperimentalMaterialApi
@Composable
fun CharacterCard(
    character: DndCharacter,
    onClick: (DndCharacter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(character) },
        modifier = modifier,
        elevation = CardElevation
    ) {
        CharacterContent(character = character)
    }
}

@ExperimentalMaterialApi
@Composable
fun EditCharacterCard(
    character: DndCharacter,
    onClick: (DndCharacter) -> Unit,
    onEditClick: (DndCharacter) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = { onClick(character) },
        modifier = modifier,
        elevation = CardElevation
    ) {
        Row (Modifier.fillMaxWidth()) {
            IconButton(onClick = {onEditClick(character)}) {
                Icon(Icons.Default.Edit, "Edit Character")
            }
            CharacterContent(character = character)
        }
    }
}

@Composable
private fun CharacterContent(character: DndCharacter) {
    Column(
        Modifier
            .fillMaxWidth()
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

@ExperimentalMaterialApi
@Composable
@Preview
private fun CharacterCardPreview() {

    DndSpellsTheme {
        Column (
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CharacterCard(
                character = sampleCharacter,
                onClick = {},
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
@Preview
private fun EditCharacterCardPreview() {

    DndSpellsTheme {
        Column (
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditCharacterCard(
                character = sampleCharacter,
                onClick = {},
                onEditClick = {},
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }
    }
}