package com.flyinpancake.dndspells

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.flyinpancake.dndspells.CharacterDetailsActivity.Companion.KEY_NAME
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.model.Spell
import com.flyinpancake.dndspells.ui.components.DndTopBar
import com.flyinpancake.dndspells.ui.components.SpellContent
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme
import com.flyinpancake.dndspells.viewmodel.CharacterViewModel
import com.flyinpancake.dndspells.viewmodel.SpellViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.random.Random


fun roll(rollText: String): String {
    var sum = 0
    val diceCount = rollText.split("d")[0].toInt()
    val diceSides = rollText.split("d")[1].toInt()

    for (ii in 1..diceCount)
        sum += Random.nextInt(from = 1, until = diceSides+1 )

    return sum.toString()
}

class CharacterDetailsActivity : ComponentActivity() {
    companion object {
        const val KEY_NAME = "KEY_NAME"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DndSpellsTheme {
                // A surface container using the 'background' color from the theme
                val viewModel = ViewModelProvider(this)
                val nameInDb = intent.getStringExtra(KEY_NAME)?:""
                val character =  viewModel[CharacterViewModel::class.java].get(nameInDb).observeAsState().value?: DndCharacter()
                val spells =
                    viewModel[SpellViewModel::class.java].allSpells.observeAsState().value ?: listOf()


                Surface(color = MaterialTheme.colors.background) {
                    CharacterDetailContent(character, spells)
                }
            }
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun CharacterDetailContent(character: DndCharacter = DndCharacter(), spells: List<Spell> = listOf()) {
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
                    intent.putExtra(KEY_NAME, character.name)
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
            character = character,
            onItemClick = { spell ->
                val intent = Intent(activity, SpellDetailsActivity::class.java)
                intent.putExtra(SpellDetailsActivity.KEY_SPELL_NAME, spell.name)
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
){
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
    character: DndCharacter,
    spells: List<Spell>,
    onItemClick: (Spell) -> Unit = {},
    onRoll: (Spell) -> Unit = {},
) {
    LazyColumn {
        items(spells.filter { character.spellList.contains(it.name) }) {
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