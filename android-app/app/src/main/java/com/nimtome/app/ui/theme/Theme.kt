package com.nimtome.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun DndSpellsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    darkColors: Colors = DarkPurpleColorPalette,
    lightColors: Colors = LightPurpleColorPalette,
    content: @Composable() () -> Unit,
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

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !colors.isLight
    val systemBarColor = colors.primary
    SideEffect {
        systemUiController.setStatusBarColor(
            color = systemBarColor,
            darkIcons = useDarkIcons
        )

        systemUiController.setNavigationBarColor(
            color = colors.background,
        )
    }
}

