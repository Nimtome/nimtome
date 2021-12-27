package com.flyinpancake.dndspells

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.flyinpancake.dndspells.model.DndCharacter
import com.flyinpancake.dndspells.ui.components.CharacterCard
import com.flyinpancake.dndspells.ui.theme.CharacterListTopbarColors
import com.flyinpancake.dndspells.ui.theme.DndSpellsTheme
import com.flyinpancake.dndspells.viewmodel.CharacterViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CharacterListActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val spellList = ViewModelProvider(this)[CharacterViewModel::class.java].allCharacters.observeAsState()

            DndSpellsTheme {
                Surface(color = MaterialTheme.colors.background) {
                    CharacterListActivityContent(spellList.value)
                }
            }
        }
    }
}

enum class MainMenuElements {
    CHARACTERS,
    SPELLS,
}

@ExperimentalMaterialApi
@Composable
fun CharacterListActivityContent(characterList: List<DndCharacter>?) {

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    var menuSelection by remember { mutableStateOf(MainMenuElements.CHARACTERS)}

    BackdropScaffold(
        appBar = {
          CenterAlignedTopAppBar(
              title =  { Text(text = "D&D spells") },
              colors = CharacterListTopbarColors(),
              navigationIcon = {
                  IconButton(onClick = { scaffoldState.switch(scope) }) {
                      Icon(Icons.Outlined.Menu, "Open Menu")
                  }
              }
          )
    }, backLayerContent = {
                          Column(
                              Modifier
                                  .fillMaxWidth(.8f)
                                  .selectableGroup()) {
                              Row (verticalAlignment = Alignment.CenterVertically)
                              {
                                  androidx.compose.material3.RadioButton(
                                      selected = menuSelection == MainMenuElements.SPELLS,
                                      onClick = { menuSelection = MainMenuElements.SPELLS })

                                  Text("Spells")
                              }

                              Row (verticalAlignment = Alignment.CenterVertically) {
                                  androidx.compose.material3.RadioButton(
                                      selected = menuSelection == MainMenuElements.CHARACTERS,
                                      onClick = { menuSelection = MainMenuElements.CHARACTERS })

                                  Text("Characters")

                              }

                              Spacer(modifier = Modifier.padding(bottom = 15.dp))

                          }
    }, frontLayerContent = {
            if (menuSelection == MainMenuElements.CHARACTERS)
                CharacterList(list = characterList)
            else
                Greeting("Android")
    },  headerHeight = 32.dp,
        scaffoldState = scaffoldState
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
private fun CharacterList(list: List<DndCharacter>?) {
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
                        CharacterCard(
                            character = character,
                            onClick = {},
                            modifier = Modifier.fillMaxWidth(.9f).padding(5.dp)
                        )
                    }
                )
            })
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
        CharacterListActivityContent(listOf(sampleCharacter))
    }
}