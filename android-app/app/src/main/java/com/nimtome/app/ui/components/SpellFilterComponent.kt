package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.ui.logic.MAX_SPELL_LEVEL
import com.nimtome.app.ui.logic.SpellFilter

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
            value = spellFilter.levelFilter / MAX_SPELL_LEVEL,
            onValueChange = { onSpellFilterChanged(spellFilter.copy(levelFilter = it * MAX_SPELL_LEVEL)) },
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
                onCheckedChange = { onSpellFilterChanged(spellFilter.copy(classFilterEnabled = it)) }
            )
            Text(
                text = "Show spells only for own class"
            )
        }
    }
}
