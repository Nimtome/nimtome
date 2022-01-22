package com.nimtome.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nimtome.app.ui.theme.CARD_INNER_FILL_RATIO
import com.nimtome.app.ui.theme.ColorPalette

@Composable
fun ColorPaletteSelector(
    modifier: Modifier = Modifier,
    selected: ColorPalette,
    onChanged: (ColorPalette) -> Unit,
) {
    Card(
        modifier,
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        Column (modifier = Modifier
            .fillMaxWidth(CARD_INNER_FILL_RATIO)
            .padding(2.dp)) {
            Text(
                text = "Preferred color palette",
            )

            Spacer(modifier = Modifier.padding(5.dp))


            LazyRow(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp)
            ) {
                this.items(
                    items = ColorPalette.values(),
                    itemContent = { colorPalette ->
                        ColorSelectorRadio(
                            selected = colorPalette == selected,
                            onClick = { onChanged(colorPalette) },
                            colors = colorPalette.lightColors,
                        )
                    }
                )
            }
        }

    }
}

@Preview
@Composable
fun ColorPaletteSelectorPreview() {
    var colorPalette by remember { mutableStateOf(ColorPalette.Purple) }
    Surface {
        ColorPaletteSelector(
            selected = colorPalette,
            onChanged = { colorPalette = it },
            modifier = Modifier.fillMaxWidth(CARD_INNER_FILL_RATIO)
        )
    }
}