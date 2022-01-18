package com.nimtome.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun DndSpellsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    darkColors: Colors = DarkPurpleColorPalette,
    lightColors: Colors = LightPurpleColorPalette,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColors
    } else {
        lightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
