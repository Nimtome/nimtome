package com.nimtome.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.model.SpellImporter
import com.nimtome.app.ui.components.CharacterCard
import com.nimtome.app.ui.components.EditCharacterCard
import com.nimtome.app.ui.components.MainMenuSpellCard
import com.nimtome.app.ui.theme.CharacterListTopbarColors
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterViewModel
import com.nimtome.app.viewmodel.SpellViewModel
import kotlinx.coroutines.*

class CharacterListActivity : ComponentActivity() {
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var spellsViewModel: SpellViewModel

    @OptIn(DelicateCoroutinesApi::class)
    private val getSpellsFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        val resolver = this@CharacterListActivity.contentResolver

                        resolver.openInputStream(uri)?.let { inputStream ->
                            val importer = SpellImporter()
                            val spellList = importer.importSpells(inputStream)

                            //Show snakbar
                            spellsViewModel.nuke()
                            spellList.forEach { spell ->
                                spellsViewModel.insert(spell)
                            }
                        }
                    }
                }
            }
        }

    private val getFilePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openSpellsFilePicker()
            }
        }

    private var showDialog = mutableStateOf(false)

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            characterViewModel = ViewModelProvider(this)[CharacterViewModel::class.java]
            spellsViewModel = ViewModelProvider(this)[SpellViewModel::class.java]

            val characterList by characterViewModel.allCharacters.observeAsState()
            val spellList by spellsViewModel.allSpells.observeAsState()

            DndSpellsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityContent(
                        characterList = characterList,
                        spellList = spellList,
                        importSpells = { handleRequestPermission() },
                        addCharacter = {
                            startActivity(
                                Intent(
                                    this,
                                    CreateCharacterActivity::class.java
                                )
                            )
                        },
                        modifyCharacter = {
                            val intent = Intent(this, CreateCharacterActivity::class.java)
                            intent.putExtra(CreateCharacterActivity.KEY_CHARACTER_NAME, it.name)
                            startActivity(intent)
                        },
                        openCharacterDetails = {
                            val intent = Intent(this, CharacterDetailsActivity::class.java)
                            intent.putExtra(CharacterDetailsActivity.KEY_NAME, it.name)
                            startActivity(intent)
                        },
                        openSpellDetails = {
                            startActivity(
                                Intent(this, SpellDetailsActivity::class.java)
                                    .putExtra(SpellDetailsActivity.KEY_SPELL_NAME, it.name)
                            )
                        },
                    )
                    if (showDialog.value)
                        StorageAccessRationaleDialog(
                            closeDialog = { showDialog.value = false },
                            importSpells = { requestStoragePermission() }
                        )
                }
            }
        }
    }

    private fun handleRequestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                openSpellsFilePicker()
            }

            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showRationaleDialog()
            }
            else -> {
                requestStoragePermission()
            }
        }
    }

    private fun showRationaleDialog() {
        showDialog.value = true
    }

    private fun openSpellsFilePicker() {
        getSpellsFile.launch(arrayOf("*/*"))
    }

    private fun requestStoragePermission() {
        getFilePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    }
}

enum class MainMenuElements {
    CHARACTERS,
    SPELLS,
}

@Composable
private fun StorageAccessRationaleDialog(
    closeDialog: () -> Unit,
    importSpells: () -> Unit
) {

    AlertDialog(
        onDismissRequest = { closeDialog() },
        title = {
            Text("Storage access needed")
        },
        confirmButton = {
            TextButton(onClick = {
                importSpells()
                closeDialog()
            }) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                closeDialog()
            }) {
                Text(text = "Cancel")
            }
        },
        text = {
            Text("We need to use storage to access the spell list XML file. If you cancel this, you have to go into setting and enable storage access.")
        }

    )
}

@ExperimentalMaterialApi
@Composable
private fun MainActivityContent(
    characterList: List<DndCharacter>?,
    spellList: List<Spell>?,
    importSpells: () -> Unit,
    openCharacterDetails: (DndCharacter) -> Unit = {},
    addCharacter: () -> Unit = {},
    modifyCharacter: (DndCharacter) -> Unit = {},
    openSpellDetails: (Spell) -> Unit = {},
    modifySpell: (Spell) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    var menuSelection by remember { mutableStateOf(MainMenuElements.CHARACTERS) }

    var isEditMode by remember { mutableStateOf(false) }

    BackdropScaffold(
        appBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "D&D spells") },
                colors = CharacterListTopbarColors(),
                navigationIcon = {
                    IconButton(onClick = { scaffoldState.switch(scope) }) {
                        Icon(Icons.Outlined.Menu, "Open Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { importSpells() }) {
                        Icon(painterResource(id = R.drawable.application_import), "Import spells")
                    }
                    IconButton(onClick = { addCharacter() }) {
                        Icon(Icons.Default.Add, "Add Character")
                    }

                }
            )
        },
        backLayerContent = {
            Column(
                Modifier
                    .fillMaxWidth(.8f)
                    .selectableGroup()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically)
                {
                    androidx.compose.material3.RadioButton(
                        selected = menuSelection == MainMenuElements.SPELLS,
                        onClick = { menuSelection = MainMenuElements.SPELLS })

                    Text("Spells")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    androidx.compose.material3.RadioButton(
                        selected = menuSelection == MainMenuElements.CHARACTERS,
                        onClick = { menuSelection = MainMenuElements.CHARACTERS })

                    Text("Characters")

                }

                Spacer(modifier = Modifier.padding(bottom = 15.dp))

            }
        },
        frontLayerContent = {
            if (menuSelection == MainMenuElements.CHARACTERS)
                CharacterList(
                    list = characterList,
                    isEditMode = isEditMode,
                    onClick = { openCharacterDetails(it) },
                    onEditClick = { modifyCharacter(it) },
                )
            else
                SpellList(
                    list = spellList,
                    onClick = { openSpellDetails(it) },
                    isEditMode = isEditMode,
                    onEditClick = {
                        scope.launch {
                            scaffoldState.snackbarHostState
                                .showSnackbar(message = "Function not yet ready")
                        }
                    },
                )
        },
        headerHeight = 32.dp,
        scaffoldState = scaffoldState,

        ) {
    }
}

@ExperimentalMaterialApi
private fun BackdropScaffoldState.switch(scope: CoroutineScope) {
    if (isConcealed)
        scope.launch { reveal() }
    else
        scope.launch { conceal() }
}

@ExperimentalMaterialApi
@Composable
private fun CharacterList(
    list: List<DndCharacter>?,
    isEditMode: Boolean,
    onClick: (DndCharacter) -> Unit = {},
    onEditClick: (DndCharacter) -> Unit
) {
    if (list == null) {
        CircularProgressIndicator()
    } else
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Select your character",
                style = MaterialTheme.typography.h5
            )
            LazyColumn(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    items(items = list,
                        itemContent = { character ->
                            if (!isEditMode)
                                CharacterCard(
                                    character = character,
                                    onClick = { onClick(it) },
                                    modifier = Modifier
                                        .fillMaxWidth(.9f)
                                        .padding(5.dp),
                                )
                            else
                                EditCharacterCard(
                                    character = character,
                                    onClick = { onClick(it) },
                                    onEditClick = { onEditClick(it) },
                                    modifier = Modifier
                                        .fillMaxWidth(.9f)
                                        .padding(5.dp)
                                )
                        }
                    )
                })
        }

}

@ExperimentalMaterialApi
@Composable
private fun SpellList(
    list: List<Spell>?,
    onClick: (Spell) -> Unit,
    onEditClick: (Spell) -> Unit,
    isEditMode: Boolean,
) {
    if (list == null) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
        }
    } else {
        Column {
            Spacer(modifier = Modifier.padding(11.dp))
            LazyColumn(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    items(items = list,
                        itemContent = { spell ->
                            MainMenuSpellCard(
                                spell = spell,
                                onClick = { onClick(spell) },
                                isEditMode = isEditMode,
                                onEditClick = { onEditClick(spell) },
                                modifier = Modifier
                                    .fillMaxWidth(.9f)
                                    .padding(5.dp)
                            )
                        }
                    )
                }
            )
        }

    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun CharacterListPreview() {
    DndSpellsTheme {
        var showDialog by remember {
            mutableStateOf(false)
        }
        MainActivityContent(
            characterList = listOf(sampleCharacter),
            spellList = sampleSpells,
            importSpells = {},
            addCharacter = {},
            modifyCharacter = {},
        )
        if (showDialog)
            StorageAccessRationaleDialog(
                importSpells = {},
                closeDialog = {
                    showDialog = false
                },
            )
    }
}