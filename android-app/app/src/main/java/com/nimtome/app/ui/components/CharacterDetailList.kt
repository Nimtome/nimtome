package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.sampleCharacter
import com.nimtome.app.ui.components.LevelSelector
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import com.nimtome.app.ui.theme.DndSpellsTheme

@ExperimentalMaterialApi
@Composable
fun CharacterDetailList(
    modifier: Modifier = Modifier,
    dndCharacter: DndCharacter,
    onChangeDndCharacter: (DndCharacter) -> Unit,
) {
    val classList = DndClass.values().toList().subList(0, DndClass.values().size - 1)

    Column (
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = dndCharacter.name,
            onValueChange = {
                onChangeDndCharacter(dndCharacter.copy(name = it))
            },
            modifier = Modifier
                .fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.character_name)) }
        )

        Spacer(modifier = Modifier.padding(8.dp))

        ClassSelector(
            modifier = Modifier.fillMaxWidth(),
            allClasses = classList,
            onClassChange = {
                onChangeDndCharacter(dndCharacter.copy(dndClass = it))
            },
            starterClass = dndCharacter.dndClass
        )

        Spacer(modifier = Modifier.padding(8.dp))

        LevelSelector(
            modifier = Modifier.fillMaxWidth(),
            level = dndCharacter.level,
            onLevelChange = {
                onChangeDndCharacter(dndCharacter.copy(level = it))
            },
        )

        Spacer(Modifier.padding(bottom = 8.dp))

        Row (
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(text = "Preferred color palette")
        }

        Spacer(Modifier.padding(bottom = 4.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ColorPaletteSelector(
                selected = dndCharacter.preferredColorPalette,
                onChanged = { onChangeDndCharacter(dndCharacter.copy(preferredColorPalette = it)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CharacterDetailListPreview() {
    Surface {
        DndSpellsTheme {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CharacterDetailList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    dndCharacter = sampleCharacter,
                    onChangeDndCharacter = {}
                )
            }

        }
    }

}
