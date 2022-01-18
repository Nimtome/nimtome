package com.nimtome.app.ui.components

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.nimtome.app.DndApplication.Companion.colorPalette
import com.nimtome.app.ui.theme.DndSpellsTheme

@Composable
fun NimtomeApp(
    darkColors: Colors = colorPalette.darkColors,
    lightColors: Colors = colorPalette.lightColors,
    component: @Composable () -> Unit,
) {
    DndSpellsTheme(
        darkColors = darkColors,
        lightColors = lightColors,
    ) {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !MaterialTheme.colors.isLight
        val systemBarColor = MaterialTheme.colors.primary
        val bgColor = MaterialTheme.colors.background

        SideEffect {
            systemUiController.setStatusBarColor(
                color = systemBarColor,
                darkIcons = useDarkIcons
            )

            systemUiController.setNavigationBarColor(
                color = bgColor,
            )
        }
        Surface(color = MaterialTheme.colors.background) {
            component()
        }
    }
}
