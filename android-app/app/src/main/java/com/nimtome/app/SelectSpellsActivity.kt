package com.nimtome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.CharacterDetailsActivity.Companion.KEY_NAME
import com.nimtome.app.DndApplication.Companion.colorPalette
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.DndClass
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.SpellContent
import com.nimtome.app.ui.components.SpellFilterComponent
import com.nimtome.app.ui.logic.SpellFilter
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterViewModel
import com.nimtome.app.viewmodel.SpellViewModel
import kotlinx.coroutines.launch

class SelectSpellsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val vmp = ViewModelProvider(this)
            val spells by
                vmp[SpellViewModel::class.java].allSpells.observeAsState(listOf())
            val characterName = intent.getStringExtra(KEY_NAME) ?: ""
            val character by
                vmp[CharacterViewModel::class.java].get(characterName).observeAsState()

            val preferredColorPalette = character?.preferredColorPalette ?: colorPalette
            DndSpellsTheme(
                darkColors = preferredColorPalette.darkColors,
                lightColors = preferredColorPalette.lightColors,
            ) {
                SelectSpellsContent(
                    spells = spells,
                    character = character ?: DndCharacter(),
                    updateCharacter = { vmp[CharacterViewModel::class.java].update(it) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SelectSpellsContent(
    spells: List<Spell>,
    character: DndCharacter,
    updateCharacter: (DndCharacter) -> Unit = {},
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
                        checked = character.spellList.contains(spell.name),
                        onCheck = { newContains ->
                            val newSpellList = character.spellList.toMutableList()
                            if (newContains && !newSpellList.contains(spell.name)) {
                                newSpellList.add(spell.name)
                            } else if (newSpellList.contains(spell.name)) {
                                newSpellList.remove(spell.name)
                            }
                            updateCharacter(character.copy(spellList = newSpellList))
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
    spellList = listOf("Power Word Kill")
)

@Preview(showBackground = true)
@Composable
fun SpellSelectPreview() {

    DndSpellsTheme {
        SelectSpellsContent(spells = sampleSpells, character = sampleCharacter)
    }
}

@Preview(showBackground = true)
@Composable
fun SpellSelectDarkPreview() {

    DndSpellsTheme(darkTheme = true) {
        SelectSpellsContent(spells = sampleSpells, character = sampleCharacter)
    }
}
