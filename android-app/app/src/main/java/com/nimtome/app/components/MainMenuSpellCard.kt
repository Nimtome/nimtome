package com.flyinpancake.dndspells.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flyinpancake.dndspells.model.Spell
import com.flyinpancake.dndspells.sampleSpells
import com.flyinpancake.dndspells.ui.theme.CardElevation
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme


@ExperimentalMaterialApi
@Composable
fun MainMenuSpellCard(
    spell: Spell,
    modifier: Modifier = Modifier,
    onClick: (Spell) -> Unit,
    onEditClick: (Spell) -> Unit,
    isEditMode: Boolean = false
) {
    Card(
        elevation = CardElevation,
        modifier = modifier,
        onClick = {onClick(spell)}
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                if (isEditMode)
                    IconButton(
                        onClick = { onClick(spell) },
                        Modifier.padding(end = 5.dp)
                    ) {
                        Icon(Icons.Default.Edit, "Edit Spell")
                    }
                SpellContent(spell = spell)
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun SpellCardNormalPreview() {

    DndSpellsTheme {
        Column (
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuSpellCard(
                spell = sampleSpells.first(),
                onClick = {},
                modifier = Modifier.fillMaxWidth(.9f),
                onEditClick = {}
            )
        }
    }
}
@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun SpellCardEditPreview() {

    DndSpellsTheme {
        Column (
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuSpellCard(
                spell = sampleSpells.first(),
                onClick = {},
                modifier = Modifier.fillMaxWidth(.9f),
                isEditMode = true,
                onEditClick = {}
            )
        }
    }
}

