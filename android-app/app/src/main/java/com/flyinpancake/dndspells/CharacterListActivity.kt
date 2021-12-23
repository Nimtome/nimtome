package com.flyinpancake.dndspells

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.flyinpancake.dndspells.ui.components.DndTopBar
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme

class CharacterListActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DndSpellsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    CharacterListActivityContent()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun CharacterListActivityContent() {
    BackdropScaffold(appBar = {
        DndTopBar("D&D Spellbook - Select a character")
    }, backLayerContent = {
        Greeting("Android")
    }, frontLayerContent = {
        Greeting("Android")
    }) {
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DndSpellsTheme {
        CharacterListActivityContent()
    }
}