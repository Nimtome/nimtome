package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.ui.theme.DndSpellsTheme

@Composable
fun LevelSelector(
    modifier: Modifier = Modifier,
    level: Int,
    onLevelChange: (Int) -> Unit,
) {
    Column(modifier = modifier) {
        Row {
            Text(
                text = stringResource(id = R.string.character_level),
                style = MaterialTheme.typography.body1
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = "$level",
                style = MaterialTheme.typography.body1,
            )
        }
        LevelSlider(level) {
            onLevelChange(it)
        }
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
    )
}

@Preview
@Composable
fun LevelSelectorPreview() {
    Surface {
        DndSpellsTheme {
            Column {
                LevelSelector(level = 4, onLevelChange = {})
            }
        }
    }
}
