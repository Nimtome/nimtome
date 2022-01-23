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
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.R
import com.nimtome.app.functions.presentation.sampleCharacter
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.ui.components.CharacterDetailList
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.theme.DndSpellsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModifyCharacterActivity : ComponentActivity() {

    private val viewModel: CreateModifyCharacterViewModel by viewModels()

    companion object {
        const val KEY_CHARACTER_ID = "KEY_CHARACTER_ID"
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var errors: List<CharacterValidationError> = emptyList()

        viewModel.event.observe(this) {
            when (it) {
                CreateModifyCharacterEvent.CharacterOk -> errors = emptyList()
                is CreateModifyCharacterEvent.CharacterValidationFailed -> errors = it.errors
                CreateModifyCharacterEvent.FinishActivity -> finish()
                null -> {}
            }
        }

        val characterId = intent.getIntExtra(KEY_CHARACTER_ID, -1)

        if (characterId == -1)
            finish()

        setContent {
            if (viewModel.state.value.character.dndClass == DndClass.None)
                viewModel.submitBaseCharacterId(characterId)
            NimtomeApp(
                colorPalette = viewModel.state.value.preferredColorPalette
            ) {
                ModifyCharacterScreenContent(
                    character = viewModel.state.value.character,
                    updateCharacter = { viewModel.updateCharacter(it) },
                    onDelete = { viewModel.deleteCharacter() },
                    submitCharacter = { viewModel.submitCharacter() }
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ModifyCharacterScreenContent(
    character: DndCharacter,
    updateCharacter: (DndCharacter) -> Unit,
    onDelete: () -> Unit,
    submitCharacter: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    val topText = stringResource(R.string.modify_character)

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DndTopBar(
                topText,
                actions = {
                    IconButton(onClick = onDelete ) {
                        Icon(Icons.Outlined.Delete, "delet character")
                    }
                }
            )
        },
        content = {
            Column {
                CharacterDetailList(
                    modifier = Modifier.padding(10.dp),
                    dndCharacter = character,
                    onChangeDndCharacter = { updateCharacter(it) },
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(stringResource(R.string.modify_character)) },
                onClick = submitCharacter,
                icon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
            )
        }
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ModifyCharacterPreview() {
    DndSpellsTheme {
        Surface {
            ModifyCharacterScreenContent(
                character = sampleCharacter,
                updateCharacter = {},
                onDelete = {},
                submitCharacter = {}
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ModifyCharacterDarkPreview() {
    DndSpellsTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            ModifyCharacterScreenContent(
                character = sampleCharacter,
                updateCharacter = {},
                onDelete = {},
                submitCharacter = {}
            )
        }
    }
}