package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class MainMenuElement(val legibleName: String) {

    CHARACTERS("Characters"),
    SPELLS("Spells"), ;
}

@ExperimentalMaterialApi
@Composable
fun MainMenuContentSelector(
    modifier: Modifier = Modifier,
    selectedElement: MainMenuElement,
    onSelectedElementChanged: (MainMenuElement) -> Unit,
) {
    LazyColumn(
        modifier,
        contentPadding = PaddingValues(1.dp)
    ) {
        items(MainMenuElement.values()) { mainMenuElement ->
            MainMenuContentSelectorButton(
                modifier = Modifier.fillMaxWidth(),
                selected = mainMenuElement == selectedElement,
                onClick = { onSelectedElementChanged(mainMenuElement) },
                text = mainMenuElement.legibleName
            )
        }
    }
}