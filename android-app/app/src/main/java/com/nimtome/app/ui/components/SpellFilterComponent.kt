package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.util.toRange
import com.nimtome.app.R
import com.nimtome.app.ui.logic.MAX_SPELL_LEVEL
import com.nimtome.app.ui.logic.SpellFilter
import kotlin.math.roundToInt

@ExperimentalMaterialApi
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

            Text("Level ${spellFilter.levelFilter.toRange().lower.roundToInt()} - ${spellFilter.levelFilter.toRange().upper.roundToInt()}")
        }
//        Slider(
//            value = spellFilter.levelFilterLower / MAX_SPELL_LEVEL,
//            onValueChange = { onSpellFilterChanged(spellFilter.copy(levelFilterLower = it * MAX_SPELL_LEVEL)) },
//            enabled = spellFilter.levelFilterEnabled
//        )
//
        RangeSlider(
            values = spellFilter.levelFilter,
            steps = 9,
            onValueChange = { onSpellFilterChanged(spellFilter.copy(levelFilter = it)) },
            enabled = spellFilter.levelFilterEnabled,
            valueRange = 0f..MAX_SPELL_LEVEL.toFloat()
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
