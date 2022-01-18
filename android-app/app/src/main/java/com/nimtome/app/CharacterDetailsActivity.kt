package com.nimtome.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.DndApplication.Companion.colorPalette
import com.nimtome.app.model.DndCharacter
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.components.SpellContent
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.CharacterSpellViewModel
import com.nimtome.app.viewmodel.CharacterViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

fun roll(rollText: String): String {
    var sum = 0
    val diceCount = rollText.split("d")[0].toInt()
    val diceSides = rollText.split("d")[1].toInt()

    repeat(diceCount) {
        sum += Random.nextInt(from = 1, until = diceSides + 1)
    }

    return sum.toString()
}

class CharacterDetailsActivity : ComponentActivity() {
    companion object {
        const val KEY_CHR_ID = "KEY_CHR_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModelProvider = ViewModelProvider(this)
            val characterId = intent.getIntExtra(KEY_CHR_ID, 0)
            val character by viewModelProvider[CharacterViewModel::class.java]
                .get(characterId).observeAsState(DndCharacter())

            val spells by viewModelProvider[CharacterSpellViewModel::class.java]
                .getSpellsForCharacter(characterId).observeAsState(listOf())

            val preferredColorPalette = character.preferredColorPalette

            NimtomeApp(
                darkColors = preferredColorPalette.darkColors,
                lightColors = preferredColorPalette.lightColors
            ) {
                Surface(color = MaterialTheme.colors.background) {
                    CharacterDetailContent(character, spells)
                }
            }
        }
    }
}

@Composable
fun CharacterDetailContent(
    character: DndCharacter = DndCharacter(),
    spells: List<Spell> = listOf(),
) {
    val activity = (LocalContext.current as? Activity)

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { DndTopBar(character.name) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Select spells") },
                onClick = {
                    val intent = Intent(activity, SelectSpellsActivity::class.java)
                    intent.putExtra(SelectSpellsActivity.KEY_CHR_ID, character.id)
                    activity?.startActivity(intent)
                },
                icon = {
                    Icon(Icons.Outlined.Add, null)
                }
            )
        },
        scaffoldState = scaffoldState,
    ) {
        val youRolled = stringResource(R.string.you_rolled)
        CharacterSpellList(
            spells = spells,
            onItemClick = { spell ->
                val intent = Intent(activity, SpellDetailsActivity::class.java)
                intent.putExtra(SpellDetailsActivity.KEY_SPELL_ID, spell.id)
                activity?.startActivity(intent)
            },
            onRoll = { spell ->
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(youRolled + roll(spell.roll!!.trim()))
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SpellListItem(
    spell: Spell,
    onItemClick: (Spell) -> Unit = {},
    onRoll: (Spell) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 5.dp),
        onClick = { onItemClick(spell) }
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            spell.roll?.let {
                IconButton(
                    onClick = { onRoll(spell) },
                    Modifier.padding(end = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.dice_5_outline),
                        contentDescription = "Roll",
                    )
                }
            }
            SpellContent(spell = spell)
        }
    }
}

@Composable
fun CharacterSpellList(
    spells: List<Spell>,
    onItemClick: (Spell) -> Unit = {},
    onRoll: (Spell) -> Unit = {},
) {
    LazyColumn {
        items(spells) {
            SpellListItem(it, onItemClick, onRoll)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CharacterDetailPreview() {
    DndSpellsTheme {
        CharacterDetailContent(sampleCharacter, sampleSpells)
    }
}
