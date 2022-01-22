package com.nimtome.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@Composable
fun MainMenuContentSelectorButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
) {
    val backgroundColor: Color
    val outlineColor: Color
    val textColor: Color
    if (selected) {
        backgroundColor = MaterialTheme.colors.primaryVariant
        outlineColor = Color.Transparent
        textColor = MaterialTheme.colors.onPrimary
    } else {
        backgroundColor = MaterialTheme.colors.primary
        outlineColor = MaterialTheme.colors.primary
        textColor = MaterialTheme.colors.onPrimary
    }

    Card(
        modifier = modifier,
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        border = BorderStroke(2.dp, outlineColor),
        onClick = onClick,
    ) {
        Row(
            Modifier
                .padding(3.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ContentSelectedButtonPreview() {
    MainMenuContentSelectorButton(
        selected = true,
        onClick = { /*TODO*/ },
        text = "Spells"
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun ContentUnselectedButtonPreview() {
    MainMenuContentSelectorButton(
        selected = false,
        onClick = { /*TODO*/ },
        text = "Spells"
    )
}



