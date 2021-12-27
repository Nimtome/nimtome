package com.flyinpancake.dndspells.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun DndSpellsTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

class CharacterListTopbarColors() : TopAppBarColors {
    @Composable
    override fun actionIconContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.centerAlignedTopAppBarColors().actionIconContentColor(
            scrollFraction = scrollFraction
        )
    }
    @Composable
    override fun containerColor(scrollFraction: Float): State<Color> {
        val color = MaterialTheme.colors.primary
        return remember {mutableStateOf(color)}
    }

    @Composable
    override fun navigationIconContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.centerAlignedTopAppBarColors().navigationIconContentColor(scrollFraction = scrollFraction)
    }

    @Composable
    override fun titleContentColor(scrollFraction: Float): State<Color> {
        return TopAppBarDefaults.centerAlignedTopAppBarColors().titleContentColor(scrollFraction = scrollFraction)
    }

}