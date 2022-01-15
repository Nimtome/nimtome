package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimtome.app.R

@Composable
fun LevelSelector(
    modifier: Modifier,
    level: Int,
    onLevelChange: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(start = 5.dp, top = 5.dp, end = 5.dp)
    ) {
        Text(
            text = stringResource(id = R.string.character_level),
            style = MaterialTheme.typography.body1
        )
        Text(text = "$level", style = MaterialTheme.typography.body1)
    }
    LevelSlider(level) {
        onLevelChange(it)
    }
}

private const val LEVELS_COUNT = 20f

@Composable
private fun LevelSlider(
    starterValue: Int,
    onValueChange: (Int) -> Unit,
) {
    var sliderPosition by remember { mutableStateOf(starterValue / LEVELS_COUNT) }

    Slider(
        value = sliderPosition,
        onValueChange = {
            sliderPosition = it
            onValueChange((sliderPosition * LEVELS_COUNT).toInt())
        },
        modifier = Modifier
            .padding(horizontal = 35.dp)
    )
}
