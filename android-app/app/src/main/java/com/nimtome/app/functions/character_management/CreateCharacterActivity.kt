package com.nimtome.app.functions.character_management

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nimtome.app.R
import com.nimtome.app.functions.presentation.sampleCharacter
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.ui.components.CharacterDetailList
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.theme.DndSpellsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCharacterActivity : ComponentActivity() {

    private val viewModel: CreateModifyCharacterViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dndCharacter = viewModel.state.value.character
            NimtomeApp(
                colorPalette = viewModel.state.value.character.preferredColorPalette
            ) {
                CreateCharacterScreenContent(
                    character = dndCharacter,
                    onChangeDndCharacter = { viewModel.updateCharacter(it) }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CreateCharacterScreenContent(
    character: DndCharacter = DndCharacter(),
    onChangeDndCharacter: (DndCharacter) -> Unit = {},
) {

    val scaffoldState = rememberScaffoldState()
    val topText = stringResource(id = R.string.title_activity_create_character)
    val viewModel: CreateModifyCharacterViewModel = hiltViewModel()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { DndTopBar(topText) },
        content = {
            Column {
                CharacterDetailList(
                    modifier = Modifier.padding(10.dp),
                    dndCharacter = character,
                    onChangeDndCharacter = { onChangeDndCharacter(it.copy()) },
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.add_new_character)) },
                onClick = { viewModel.submitCharacter() },
                icon = { Icon(Icons.Outlined.Add, contentDescription = null) }
            )
        }
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun CreateCharacterPreview() {
    DndSpellsTheme {
        Surface {
            CreateCharacterScreenContent(character = sampleCharacter, onChangeDndCharacter = { })
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun CreateCharacterDarkPreview() {
    DndSpellsTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            CreateCharacterScreenContent(character = sampleCharacter, onChangeDndCharacter = { })
        }
    }
}
