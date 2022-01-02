package com.nimtome.app

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.ui.components.ClassSelector
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.LevelSelector
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

lateinit var characterViewModel: CharacterViewModel

fun addNewCharacter(character: DndCharacter) {
    characterViewModel.insert(character)
}


class CreateCharacterActivity : ComponentActivity() {

    companion object {
        const val KEY_CHARACTER_NAME = "KEY_CHARACTER_NAME"
    }

    private var characterLiveData: LiveData<DndCharacter>? = null

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
        val characterName = intent.getStringExtra(KEY_CHARACTER_NAME)
        val isCreateMode = characterName.isNullOrBlank()

        setContent {
            MyApp {
                var dndCharacter: DndCharacter? by remember { mutableStateOf(null) }
                characterLiveData = characterName?.let { characterViewModel.get(it) }
                val originalCharacter = characterLiveData?.observeAsState()

                if (dndCharacter == null)
                    dndCharacter = if (!isCreateMode && originalCharacter != null) {
                        originalCharacter.value
                    } else {
                        DndCharacter()
                    }

                if (dndCharacter == null)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CircularProgressIndicator()
                    }
                else
                    CreateCharacterScreenContent(
                        isCreateMode,
                        character = dndCharacter!!,
                        onChangeDndCharacter = { dndCharacter = it }
                    )
            }
        }
    }

    fun updateCharacter(character: DndCharacter) {
        characterLiveData?.removeObservers(this)
        characterViewModel.update(character)
    }

    fun deleteCharacter(character: DndCharacter) {
        characterLiveData?.removeObservers(this)
        characterViewModel.delete(character)
        finish()
    }
}

@ExperimentalMaterialApi
@Composable
fun CreateCharacterScreenContent(
    isCreateMode: Boolean = true,
    character: DndCharacter = DndCharacter(),
    onChangeDndCharacter: (DndCharacter) -> Unit = {},
) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val activity = LocalContext.current as? CreateCharacterActivity

    val topText = when (isCreateMode) {
        true -> "Create Your Character"
        false -> "Modify Your Character"
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DndTopBar(
                topText,
                actions = {
                    if (!isCreateMode) {
                        IconButton(onClick = { activity?.deleteCharacter(character) }) {
                            Icon(Icons.Outlined.Delete, "delet character")
                        }
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
            if (isCreateMode) {
                CreateCharacterFloatingActionButton(
                    dndCharacter = character,
                    activity = activity,
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            } else {
                EditCharacterFloatingActionButton(
                    dndCharacter = character,
                    activity = activity,
                    scope = scope,
                    scaffoldState = scaffoldState
                )
            }
        }
    )
}

@Composable
fun EditCharacterFloatingActionButton(
    dndCharacter: DndCharacter,
    activity: CreateCharacterActivity?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
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

@Composable
fun CreateCharacterFloatingActionButton(
    dndCharacter: DndCharacter,
    activity: Activity?,
    scope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val characterErrorText = stringResource(R.string.character_error)

    ExtendedFloatingActionButton(
        text = { Text(stringResource(R.string.add_new_character)) },
        onClick = {
            val validation = validateCharacter(dndCharacter)
            if (validation) {
                addNewCharacter(dndCharacter)
                activity?.finish()
            } else
                scope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(characterErrorText)
                }
        },
        icon = { Icon(Icons.Outlined.Add, contentDescription = null) }
    )
}

fun validateCharacter(dndCharacter: DndCharacter): Boolean {
    //General shit

    if (dndCharacter.name.isEmpty())
        return false
    if (dndCharacter.dndClass == DndClass.None)
        return false
    if (dndCharacter.level > 20 || dndCharacter.level < 0)
        return false

    return true
}

@ExperimentalMaterialApi
@Composable
fun CharacterDetailList(
    modifier: Modifier = Modifier,
    dndCharacter: DndCharacter,
    onChangeDndCharacter: (DndCharacter) -> Unit
) {
    val classList = DndClass.values().toList().subList(0, 11)

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
    }
}


@Composable
fun MyApp(component: @Composable () -> Unit) {
    DndSpellsTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
            component()
        }
    }
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