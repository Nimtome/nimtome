package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nimtome.app.model.Spell

@Composable
fun SpellContent(spell: Spell) {
    val textPadding = Modifier.padding(horizontal = 5.dp)
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = spell.name,
                modifier = textPadding,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = "Level ${spell.level}",
                modifier = textPadding,
                maxLines = 1,
                style = MaterialTheme.typography.h6
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = spell.range,
                modifier = textPadding,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = spell.time,
                modifier = textPadding,
                style = MaterialTheme.typography.body1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = spell.components,
                style = MaterialTheme.typography.body1,
                modifier = textPadding,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}