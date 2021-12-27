package com.flyinpancake.dndspells.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.sampleCharacter
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme

@ExperimentalMaterialApi
@Composable
fun CharacterCard(
    character: DndCharacter,
    onClick: (DndCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onClick(character) },
        modifier = modifier,
        elevation = 4.dp
    ) {
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
            CharacterCard(modifier = Modifier.fillMaxWidth(.9f), character = sampleCharacter, onClick = {})
        }
    }
}