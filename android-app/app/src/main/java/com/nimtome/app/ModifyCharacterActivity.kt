package com.nimtome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

class ModifyCharacterActivity : ComponentActivity() {

    companion object {
        const val KEY_CHARACTER_ID = "KEY_CHARACTER_ID"
    }

    private lateinit var characterViewmodel: CharacterViewModel

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val characterId = intent.getIntExtra(KEY_CHARACTER_ID, -1)

        if (characterId == -1)
            finish()

        characterViewmodel = ViewModelProvider(this)[CharacterViewModel::class.java]

        setContent {
            var dndCharacter: DndCharacter? by remember { mutableStateOf(null) }
            NimtomeApp(
                colorPalette = dndCharacter?.preferredColorPalette ?: DndApplication.colorPalette,
            ) {
                val originalCharacter by characterViewmodel.get(characterId).observeAsState()

                if (dndCharacter == null) {
                    originalCharacter?.let { dndCharacter = it }
                } else {
                    ModifyCharacterScreenContent(
                        character = dndCharacter!!,
                        onChangeDndCharacter = { dndCharacter = it }
                    )
                }
            }
        }
    }

    fun deleteCharacter(character: DndCharacter) {
        characterViewmodel.delete(character)
    }

    fun updateCharacter(character: DndCharacter) {
        characterViewmodel.update(character)
    }
}

@ExperimentalMaterialApi
@Composable
private fun ModifyCharacterScreenContent(
    character: DndCharacter = DndCharacter(),
    onChangeDndCharacter: (DndCharacter) -> Unit = {},
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val activity = LocalContext.current as? ModifyCharacterActivity

    val topText = "Create Your Character"

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DndTopBar(
                topText,
                actions = {
                    IconButton(onClick = { activity?.deleteCharacter(character) }) {
                        Icon(Icons.Outlined.Delete, "delet character")
                    }
                }
            )
        },
        content = {
            Column {
                CharacterDetailList(
                    dndCharacter = character,
                    onChangeDndCharacter = {
                        onChangeDndCharacter(it.copy())
                    },
                )
            }
        },
        floatingActionButton = {
            ModifyCharacterFloatingActionButton(
                dndCharacter = character,
                activity = activity,
                scope = scope,
                scaffoldState = scaffoldState
            )
        }
    )
}

@Composable
fun ModifyCharacterFloatingActionButton(
    dndCharacter: DndCharacter,
    activity: ModifyCharacterActivity?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    val characterErrorText = stringResource(R.string.character_error)

    ExtendedFloatingActionButton(
        text = { Text(stringResource(R.string.modify_character)) },
        onClick = {
            val validation = validateCharacter(dndCharacter)
            if (validation) {
                activity?.updateCharacter(dndCharacter)
                activity?.finish()
            } else
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(characterErrorText)
                }
        },
        icon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
    )
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ModifyCharacterPreview() {
    DndSpellsTheme {
        Surface {
            ModifyCharacterPreview()
        }
    }
}

@ExperimentalMaterialApi
@Preview(showBackground = true)
@Composable
private fun ModifyCharacterDarkPreview() {
    DndSpellsTheme(darkTheme = true) {
        Surface(color = MaterialTheme.colors.background) {
            ModifyCharacterPreview()
        }
    }
}
