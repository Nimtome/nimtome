package com.nimtome.app.ui.components.character_management

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.functions.presentation.sampleCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.ui.logic.MAX_CHARACTER_LEVEL
import com.nimtome.app.ui.theme.DndSpellsTheme

@Composable
fun LevelSelector(
    modifier: Modifier = Modifier,
    level: Int,
    onLevelChange: (Int) -> Unit,
    dndClass: DndClass,
    levelRange: IntRange = 0..MAX_CHARACTER_LEVEL
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                Text(
                    text = "${dndClass.legibleName} ${stringResource(id = R.string.character_level)}",
                    style = MaterialTheme.typography.body1
                )
                Text(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    text = "$level",
                    style = MaterialTheme.typography.body1,
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.height(IntrinsicSize.Min)
            ) {
                IconButton(onClick = {
                    if (levelRange.contains(level - 1))
                        onLevelChange(level - 1)
                }
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        "level down"
                    )
                }
                IconButton(onClick = {
                    if (levelRange.contains(level + 1))
                        onLevelChange(level + 1)
                }) {
                    Icon(
                        Icons.Default.ArrowForward,
                        "level up"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LevelSelectorPreview() {
    Surface {
        DndSpellsTheme {
            Column {
                LevelSelector(level = 4, onLevelChange = {}, dndClass = sampleCharacter.dndClass)
            }
        }
    }
}
