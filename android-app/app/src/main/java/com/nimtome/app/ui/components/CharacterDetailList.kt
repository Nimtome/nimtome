package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass

@ExperimentalMaterialApi
@Composable
fun CharacterDetailList(
    modifier: Modifier = Modifier,
    dndCharacter: DndCharacter,
    onChangeDndCharacter: (DndCharacter) -> Unit,
) {
    val classList = DndClass.values().toList().subList(0, DndClass.values().size - 1)

    Column {
        OutlinedTextField(
            value = dndCharacter.name,
            onValueChange = {
                onChangeDndCharacter(dndCharacter.copy(name = it))
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 10.dp),
            label = { Text(text = stringResource(id = R.string.character_name)) }
        )
        ClassSelector(
            modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 25.dp, end = 25.dp, bottom = 15.dp),
            allClasses = classList,
            onClassChange = {
                onChangeDndCharacter(dndCharacter.copy(dndClass = it))
            },
            starterClass = dndCharacter.dndClass
        )

        LevelSelector(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 0.dp),
            level = dndCharacter.level,
            onLevelChange = {
                onChangeDndCharacter(dndCharacter.copy(level = it))
            },
        )

        Spacer(Modifier.padding(bottom = 10.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ColorPaletteSelector(
                modifier = modifier.fillMaxWidth(.85f),
                selected = dndCharacter.preferredColorPalette,
                onChanged = { onChangeDndCharacter(dndCharacter.copy(preferredColorPalette = it)) }
            )
        }
    }
}
