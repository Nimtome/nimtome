package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nimtome.app.functions.home.HomeScreenSelection


@ExperimentalMaterialApi
@Composable
fun MainMenuContentSelector(
    modifier: Modifier = Modifier,
    selectedElement: HomeScreenSelection,
    onSelectedElementChanged: (HomeScreenSelection) -> Unit,
) {
    LazyColumn(
        modifier,
        contentPadding = PaddingValues(1.dp)
    ) {
        items(HomeScreenSelection.values()) { mainMenuElement ->
            MainMenuContentSelectorButton(
                modifier = Modifier.fillMaxWidth(),
                selected = mainMenuElement == selectedElement,
                onClick = { onSelectedElementChanged(mainMenuElement) },
                text = mainMenuElement.legibleName
            )
        }
    }
}