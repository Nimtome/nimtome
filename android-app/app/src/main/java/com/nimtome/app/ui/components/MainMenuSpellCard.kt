package com.nimtome.app.ui.components

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
import com.nimtome.app.functions.presentation.sampleSpells
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.theme.CARD_ELEVATION
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import com.nimtome.app.ui.theme.DndSpellsTheme

@ExperimentalMaterialApi
@Composable
fun MainMenuSpellCard(
    spell: Spell,
    modifier: Modifier = Modifier,
    onClick: (Spell) -> Unit,
    onEditClick: (Spell) -> Unit,
    isEditMode: Boolean = false,
) {
    Card(
        elevation = CARD_ELEVATION,
        modifier = modifier,
        onClick = { onClick(spell) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(Modifier.fillMaxWidth()) {
                if (isEditMode)
                    IconButton(
                        onClick = { onEditClick(spell) },
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
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuSpellCard(
                spell = sampleSpells.first(),
                onClick = {},
                modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO),
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
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MainMenuSpellCard(
                spell = sampleSpells.first(),
                onClick = {},
                modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO),
                isEditMode = true,
                onEditClick = {}
            )
        }
    }
}
