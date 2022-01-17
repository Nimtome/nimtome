package com.nimtome.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.nimtome.app.DndApplication.Companion.colorPalette
import com.nimtome.app.model.Spell
import com.nimtome.app.ui.components.DndTopBar
import com.nimtome.app.ui.components.NimtomeApp
import com.nimtome.app.ui.theme.DndSpellsTheme
import com.nimtome.app.viewmodel.SpellViewModel
import kotlinx.coroutines.launch

class SpellDetailsActivity : ComponentActivity() {
    companion object {
        const val KEY_SPELL_ID = "KEY_SPELL_ID"
        const val KEY_COLOR_OVERRIDE = "KEY_COLOR_OVERRIDE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val spellId = intent.getIntExtra(KEY_SPELL_ID, 0)

        setContent {
            val spell by ViewModelProvider(this)[SpellViewModel::class.java]
                .get(spellName)
                .observeAsState(Spell())

            NimtomeApp(
                darkColors = colorPalette.darkColors,
                lightColors = colorPalette.lightColors,
            ) {
                SpellDetails(spell = spell)
            }
        }
    }
}

@Composable
fun SpellDetails(
    spell: Spell,
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    val youRolled = stringResource(id = R.string.you_rolled)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { DndTopBar(spell.name) },
        floatingActionButton = {
            spell.roll?.let { roll ->
                ExtendedFloatingActionButton(
                    text = { Text("Cast Spell") },
                    onClick = {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(youRolled + roll(roll.trim()))
                        }
                    },
                    icon = { Icon(painterResource(id = R.drawable.dice_5_outline), null) }
                )
            }
        },
        scaffoldState = scaffoldState,
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
        ) {
            SpellAttributes(spell)
            SpellDescription(spell)
        }
    }
}

@Composable
fun SpellAttributes(spell: Spell) {
    val mod = Modifier.padding(25.dp, 0.dp)

    Text(
        text = "School: ${spell.school}",
        modifier = mod
    )
    Text(
        text = "Spell Level: ${spell.level}",
        modifier = mod
    )
    Text(
        text = "Time: ${spell.time}",
        modifier = mod
    )
    Text(
        text = "Components: ${spell.components}",
        modifier = mod
    )
    Text(
        text = "Range: ${spell.range}",
        modifier = mod
    )
    Text(
        text = "Spell school: ${spell.school}",
        modifier = mod
    )
}

@Composable
fun SpellDescription(spell: Spell) {
    Text(
        text = spell.desc.replace("\n\t", ""),
        modifier = Modifier
            .padding(25.dp)
            .fillMaxSize(),
        maxLines = 100
    )
}

@Preview(showBackground = true)
@Composable
fun SpellDetailsPreview() {
    DndSpellsTheme {
        SpellDetails(sampleSpells[0])
    }
}

@Preview(showBackground = true)
@Composable
fun SpellDetailsDarkPreview() {
    DndSpellsTheme(darkTheme = true) {
        SpellDetails(sampleSpells[0])
    }
}
