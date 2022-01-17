package com.nimtome.app

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.AlertDialog
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropScaffoldState
import androidx.compose.material.BackdropValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.DndApplication.Companion.colorPalette
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.model.SpellImporter
import com.nimtome.app.ui.components.CharacterCard
import com.nimtome.app.ui.components.ColorPaletteSelector
import com.nimtome.app.ui.components.EditCharacterCard
import com.nimtome.app.ui.components.MainMenuContentSelector
import com.nimtome.app.ui.components.MainMenuElement
import com.nimtome.app.ui.components.MainMenuSpellCard
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import com.nimtome.app.ui.theme.ColorPalette
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterViewModel
import com.nimtome.app.viewmodel.SpellViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                        runCatching {
                            resolver.openInputStream(uri)?.let { inputStream ->
                                val importer = SpellImporter()
                                val spellList = importer.importSpells(inputStream)

                                //TODO Show snakbar
                                spellsViewModel.nuke()
                                spellList.forEach { spell ->
                                    spellsViewModel.insert(spell)
                                }
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
            var colorPalette by remember { mutableStateOf(colorPalette) }

            NimtomeApp(
                darkColors = colorPalette.darkColors,
                lightColors = colorPalette.lightColors
            ) {
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
                            intent.putExtra(CreateCharacterActivity.KEY_CHARACTER_ID, it.id)
                            startActivity(intent)
                        },
                        openCharacterDetails = {
                            val intent = Intent(this, CharacterDetailsActivity::class.java)
                            intent.putExtra(CharacterDetailsActivity.KEY_CHR_ID, it.id)
                            startActivity(intent)
                        },
                        openSpellDetails = {
                            startActivity(
                                Intent(this, SpellDetailsActivity::class.java)
                                    .putExtra(SpellDetailsActivity.KEY_SPELL_ID, it.id)
                            )
                        },
                        colors = colorPalette,
                        onColorsChange = {
                            colorPalette = it
                            DndApplication.colorPalette = it
                        }
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


@Composable
private fun StorageAccessRationaleDialog(
    closeDialog: () -> Unit,
    importSpells: () -> Unit,
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
            Text(stringResource(R.string.storage_access_rationale))
        }

    )
}

@Suppress("UnusedPrivateMember")
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
    colors: ColorPalette,
    onColorsChange: (ColorPalette) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    var menuSelection by remember { mutableStateOf(MainMenuElement.CHARACTERS) }

    var isEditMode by remember { mutableStateOf(false) }

    BackdropScaffold(
        appBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "D&D spells") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colors.primary),
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
                    .fillMaxWidth()
                    .selectableGroup(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainMenuContentSelector(
                    modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO),
                    selectedElement = menuSelection,
                    onSelectedElementChanged = { menuSelection = it },
                )

                Spacer(modifier = Modifier.padding(bottom = 5.dp))

                ColorPaletteSelector(modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO), selected = colors, onChanged = onColorsChange)

                Spacer(modifier = Modifier.padding(bottom = 15.dp))
            }
        },
        frontLayerContent = {
            if (menuSelection == MainMenuElement.CHARACTERS)
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
    onEditClick: (DndCharacter) -> Unit,
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
                    items(
                        items = list,
                        itemContent = { character ->
                            if (!isEditMode)
                                CharacterCard(
                                    character = character,
                                    onClick = { onClick(it) },
                                    modifier = Modifier
                                        .fillMaxWidth(CARD_INNER_FILL_RATIO)
                                        .padding(5.dp),
                                )
                            else
                                EditCharacterCard(
                                    character = character,
                                    onClick = { onClick(it) },
                                    onEditClick = { onEditClick(it) },
                                    modifier = Modifier
                                        .fillMaxWidth(CARD_INNER_FILL_RATIO)
                                        .padding(5.dp)
                                )
                        }
                    )
                }
            )
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
                    items(
                        items = list,
                        itemContent = { spell ->
                            MainMenuSpellCard(
                                spell = spell,
                                onClick = { onClick(spell) },
                                isEditMode = isEditMode,
                                onEditClick = { onEditClick(spell) },
                                modifier = Modifier
                                    .fillMaxWidth(CARD_INNER_FILL_RATIO)
                                    .padding(5.dp)
                            )
                        }
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun CharacterListPreview() {
    DndSpellsTheme {
        MainActivityContent(
            characterList = listOf(sampleCharacter),
            spellList = sampleSpells,
            importSpells = { },
            colors = ColorPalette.Purple,
            onColorsChange = { }
        )
    }
}
