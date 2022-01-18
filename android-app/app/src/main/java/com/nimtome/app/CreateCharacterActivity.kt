package com.nimtome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.ui.components.CharacterDetailList
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.logic.validateCharacter
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch





class CreateCharacterActivity : ComponentActivity() {

    private lateinit var characterViewModel: CharacterViewModel

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]

        setContent {
            var dndCharacter by remember { mutableStateOf(DndCharacter()) }
            NimtomeApp(
                colorPalette = dndCharacter.preferredColorPalette
            ) {
                CreateCharacterScreenContent(
                    character = dndCharacter,
                    onChangeDndCharacter = { dndCharacter = it }
                )
            }
        }
    }

    fun addNewCharacter(character: DndCharacter) {
        characterViewModel.insert(character)
    }
}

@ExperimentalMaterialApi
@Composable
private fun CreateCharacterScreenContent(
    character: DndCharacter = DndCharacter(),
    onChangeDndCharacter: (DndCharacter) -> Unit = {},
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val activity = LocalContext.current as? CreateCharacterActivity

    val topText = "Create Your Character"

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { DndTopBar(topText) },
        content = {
            Column {
                CharacterDetailList(
                    dndCharacter = character,
                    onChangeDndCharacter = { onChangeDndCharacter(it.copy()) },
                )
            }
        },
        floatingActionButton = {
            CreateCharacterFloatingActionButton(
                dndCharacter = character,
                activity = activity,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
    )
}

@Composable
private fun CreateCharacterFloatingActionButton(
    dndCharacter: DndCharacter,
    activity: CreateCharacterActivity?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val characterErrorText = stringResource(R.string.character_error)

    ExtendedFloatingActionButton(
        text = { Text(stringResource(R.string.add_new_character)) },
        onClick = {
            val validation = validateCharacter(dndCharacter)
            if (validation) {
                activity?.addNewCharacter(dndCharacter)
                activity?.finish()
            } else
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(characterErrorText)
                }
        },
        icon = { Icon(Icons.Outlined.Add, contentDescription = null) }
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun CreateCharacterPreview() {
    DndSpellsTheme {
        Surface {
            CreateCharacterScreenContent()
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
fun CreateCharacterDarkPreview() {
    DndSpellsTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            CreateCharacterScreenContent()
        }
    }
}
