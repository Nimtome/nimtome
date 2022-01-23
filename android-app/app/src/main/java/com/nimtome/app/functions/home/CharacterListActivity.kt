package com.nimtome.app.functions.home

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.nimtome.app.R
import com.nimtome.app.functions.character_management.CreateCharacterActivity
import com.nimtome.app.functions.character_management.ModifyCharacterActivity
import com.nimtome.app.functions.presentation.CharacterDetailsActivity
import com.nimtome.app.functions.presentation.SpellDetailsActivity
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.model.SpellImporter
import com.nimtome.app.ui.components.*
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class CharacterListActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private val getSpellsFile =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        val resolver = this@CharacterListActivity.contentResolver
                        runCatching {
                            resolver.openInputStream(uri)?.let { inputStream ->
                                val importer = SpellImporter()
                                viewModel.submitNewSpellList(importer.importSpells(inputStream))

                                //TODO Show snakbar
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


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.viewEvent.observe(this) {
            it?.let {
                when (it) {
                    is HomeViewEvent.OpenSpellImporter -> this.handleRequestPermission()
                    is HomeViewEvent.AddCharacterEvent -> startActivity(
                        Intent(
                            this,
                            CreateCharacterActivity::class.java
                        )
                    )
                    is HomeViewEvent.ImportSpellsEvent -> TODO()
                    is HomeViewEvent.ModifyCharacter -> startActivity(
                        Intent(this, ModifyCharacterActivity::class.java)
                            .putExtra(ModifyCharacterActivity.KEY_CHARACTER_ID, it.character.id)
                    )
                    is HomeViewEvent.OpenCharacterEvent -> startActivity(
                        Intent(this, CharacterDetailsActivity::class.java)
                            .putExtra(CharacterDetailsActivity.KEY_CHR_ID, it.character.id)
                    )
                    is HomeViewEvent.OpenSpellEvent -> startActivity(
                        Intent(this, SpellDetailsActivity::class.java)
                            .putExtra(SpellDetailsActivity.KEY_SPELL_ID, it.spell.id)
                    )
                }
            }
        }

        setContent {
            val viewModel: HomeViewModel = hiltViewModel()
            val state = viewModel.state.value
            NimtomeApp(
                colorPalette = state.colorPalette
            ) {
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityContent(
                        viewModel = viewModel
                    )
                    if (state.showDialog)
                        StorageAccessRationaleDialog(
                            closeDialog = { viewModel.changeShowDialog(false) },
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

    private fun showRationaleDialog() = viewModel.changeShowDialog(true)

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
    viewModel: HomeViewModel

) {
    val state by viewModel.state

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

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
                    IconButton(onClick = { viewModel.changeEditMode(!state.editMode) }) {
                        Icon(Icons.Default.Edit, "Edit")
                    }
                    IconButton(onClick = { viewModel.importSpellsFromFile() }) {
                        Icon(painterResource(id = R.drawable.application_import), "Import spells")
                    }
                    IconButton(onClick = { viewModel.openAddCharacter() }) {
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
                    selectedElement = state.selectedList,
                    onSelectedElementChanged = { viewModel.selectContentList(it) },
                )

                Spacer(modifier = Modifier.padding(bottom = 5.dp))

                ColorPaletteSelector(
                    modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO),
                    selected = state.colorPalette,
                    onChanged = { viewModel.changePreferredColors(it) }
                )

                Spacer(modifier = Modifier.padding(bottom = 15.dp))
            }
        },
        frontLayerContent = {
            when (state.selectedList) {
                HomeScreenSelection.CHARACTERS -> CharacterList(
                    list = state.characters,
                    isEditMode = state.editMode,
                    onClick = { viewModel.openCharacterDetails(it) },
                    onEditClick = { viewModel.modifyCharacter(it) },
                )
                HomeScreenSelection.SPELLS -> SpellList(
                    list = state.spells,
                    onClick = { viewModel.openSpellDetails(it) },
                    isEditMode = state.editMode,
                    onEditClick = {
                        scope.launch {
                            scaffoldState.snackbarHostState
                                .showSnackbar(message = "Function not yet ready")
                        }
                    },
                )
            }
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
                            CharacterCard(
                                character = character,
                                onClick = { onClick(it) },
                                editMode = isEditMode,
                                onEditClick = onEditClick,
                                modifier = Modifier
                                    .fillMaxWidth(CARD_INNER_FILL_RATIO)
                                    .padding(5.dp),
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

//@OptIn(ExperimentalMaterialApi::class)
//@Preview(showBackground = true)
//@Composable
//fun CharacterListPreview() {
//    DndSpellsTheme {
//        MainActivityContent(
//            characterList = listOf(sampleCharacter),
//            spellList = sampleSpells,
//            importSpells = { },
//            colors = ColorPalette.Purple,
//            onColorsChange = { },
//            viewModel = viewModel
//        )
//    }
//}
