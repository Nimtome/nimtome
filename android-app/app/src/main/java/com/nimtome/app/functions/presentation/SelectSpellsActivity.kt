package com.nimtome.app.functions.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.DndApplication
import com.nimtome.app.R
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.components.SpellContent
import com.nimtome.app.ui.components.SpellFilterComponent
import com.nimtome.app.ui.logic.SpellFilter
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterSpellViewModel
import com.nimtome.app.viewmodel.CharacterViewModel
import com.nimtome.app.viewmodel.SpellViewModel
import kotlinx.coroutines.launch

class SelectSpellsActivity : ComponentActivity() {

    companion object {
        const val KEY_CHR_ID = "KEY_CHR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vmp = ViewModelProvider(this)
//            val spells by vmp[SpellViewModel::class.java].allSpells.observeAsState(listOf())
            val spells: List<Spell> = emptyList()
            val characterId = intent.getIntExtra(KEY_CHR_ID, 0)
            vmp[CharacterViewModel::class.java].setActiveCharacterById(characterId)
            val character by vmp[CharacterViewModel::class.java].activeCharacter.observeAsState()
            val characterSpells by vmp[CharacterSpellViewModel::class.java].getSpellsForCharacter(
                characterId
            ).observeAsState(listOf())
            val preferredColorPalette = character?.preferredColorPalette ?: DndApplication.colorPalette

            NimtomeApp(
                darkColors = preferredColorPalette.darkColors,
                lightColors = preferredColorPalette.lightColors,
            ) {
                if (character != null)
                    SelectSpellsContent(
                        spells = spells,
                        character = character!!,
                        characterSpells = characterSpells,
                        updateSpells = {
                            vmp[CharacterSpellViewModel::class.java].submitSpellist(character!!, it)
                        }
                    )
                else
                    CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectSpellsContent(
    spells: List<Spell>,
    character: DndCharacter,
    characterSpells: List<Spell>,
    updateSpells: (List<Spell>) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var spellFilter by remember { mutableStateOf(SpellFilter(classFilter = character.dndClass)) }

    BottomSheetScaffold(
        topBar = { DndTopBar(character.name + " " + stringResource(R.string.select_spells)) },
        sheetPeekHeight = 64.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(
                        text = "Swipe to Filter Spells",
                        style = MaterialTheme.typography.body1
                    )
                }
            }

            SpellFilterComponent(
                spellFilter = spellFilter,
                onSpellFilterChanged = { spellFilter = it }
            )
        },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    if (scaffoldState.bottomSheetState.isCollapsed)
                        scaffoldState.bottomSheetState.expand()
                    else if (scaffoldState.bottomSheetState.isExpanded)
                        scaffoldState.bottomSheetState.collapse()
                }
            }) {
                Icon(Icons.Outlined.Info, "filter")
            }
        }
    ) {
        LazyColumn {
            items(
                items = spellFilter.filterSpells(spells),
                itemContent = { spell ->
                    SpellCardWithCheckBox(
                        spell = spell,
                        checked = characterSpells.contains(spell),
                        onCheck = { newContains ->
                            val mutableSpells = characterSpells.toMutableList()
                            if (newContains) {
                                mutableSpells.add(spell)
                            } else {
                                mutableSpells.remove(spell)
                            }
                            updateSpells(mutableSpells)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun SpellCardWithCheckBox(
    spell: Spell,
    checked: Boolean,
    onCheck: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 5.dp)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
        ) {
            Checkbox(
                checked = checked,
                onCheckedChange = { onCheck(it) },
                Modifier.padding(end = 2.dp)
            )
            SpellContent(spell = spell)
        }
    }
}

val sampleSpells = listOf(
    Spell(
        name = "Power Word Kill",
        level = 9,
        classes = "Wizard",
        components = "V",
        desc = "Kil",
        duration = "1 action",
        range = "240 ft",
        ritual = false,
        school = "E",
        time = "instantaneous",
        roll = "10d6"
    )
)

val sampleCharacter = DndCharacter(
    name = "Ba'luk",
    level = 4,
    dndClass = DndClass.Druid,
)

@Preview(showBackground = true)
@Composable
fun SpellSelectPreview() {

    DndSpellsTheme {
        SelectSpellsContent(
            spells = sampleSpells,
            character = sampleCharacter,
            characterSpells = listOf()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SpellSelectDarkPreview() {

    DndSpellsTheme(darkTheme = true) {
        SelectSpellsContent(
            spells = sampleSpells,
            character = sampleCharacter,
            characterSpells = listOf()
        )
    }
}
