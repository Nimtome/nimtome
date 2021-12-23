package com.flyinpancake.dndspells.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.flyinpancake.dndspells.ui.logic.SpellFilter
import com.flyinpancake.dndspells.R

@Composable
fun SpellFilterComponent(
    spellFilter: SpellFilter,
    onSpellFilterChanged: (SpellFilter) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = spellFilter.nameFilter,
            onValueChange = { onSpellFilterChanged(spellFilter.copy(nameFilter = it)) },
            label = {
                Text(stringResource(R.string.spell_name))
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Divider(
            Modifier.padding(vertical = 20.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = spellFilter.levelFilterEnabled,
                    onCheckedChange = { onSpellFilterChanged(spellFilter.copy(levelFilterEnabled = it)) },
                    Modifier.padding(end = 20.dp),
                )
                Text(stringResource(R.string.filter_spell_level))
            }

            Text("Level " + spellFilter.levelFilter.toInt())
        }
        Slider(
            value = spellFilter.levelFilter / 9,
            onValueChange = { onSpellFilterChanged(spellFilter.copy(levelFilter = it * 9))},
            enabled = spellFilter.levelFilterEnabled
        )

        Divider(
            Modifier.padding(vertical = 20.dp)
        )

        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = spellFilter.componentFilterEnabled,
                onCheckedChange = { onSpellFilterChanged(spellFilter.copy(componentFilterEnabled = it)) },
                Modifier.padding(end = 20.dp)
            )
            OutlinedTextField(
                value = spellFilter.componentFilter,
                onValueChange = { onSpellFilterChanged(spellFilter.copy(componentFilter = it)) },
                label = { Text(stringResource(R.string.filter_components)) },
                enabled = spellFilter.componentFilterEnabled
            )
        }

        Divider(
            Modifier.padding(vertical = 20.dp)
        )

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Switch(
                checked = spellFilter.classFilterEnabled,
                onCheckedChange = { onSpellFilterChanged(spellFilter.copy(classFilterEnabled = it)) })
            Text(
                text = "Show spells only for own class"
            )
        }
    }
}
