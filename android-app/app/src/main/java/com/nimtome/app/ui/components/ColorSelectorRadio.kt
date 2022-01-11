package com.nimtome.app.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ColorSelectorRadio(
    selected: Boolean,
    onClick: () -> Unit,
    colors: Colors,
    backgroundColor: Color = MaterialTheme.colors.background,
) {
    Card(
        shape = CircleShape,
        backgroundColor = backgroundColor
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = colors.primary,
                unselectedColor = colors.primary),
        )
    }

}