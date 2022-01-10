package com.nimtome.app.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val DarkPurpleColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

val LightPurpleColorPalette = lightColors(
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

val DarkBlueColorPalette = darkColors(
    primary = Blue200,
    primaryVariant = Blue700,
    secondary = Teal200
)

val LightBlueColorPalette = lightColors(
    primary = Blue500,
    primaryVariant = Blue700,
    secondary = Teal200,
)

val DarkOrangeColorPalette = darkColors(
    primary = Orange300Light,
    primaryVariant = Orange300Dark,
    secondary = Red300,
    secondaryVariant = Red300Dark,
)

val LightOrangeColorPalette = lightColors(
    primary = Orange300,
    primaryVariant = Orange300Dark,
    secondary = Red300,
    secondaryVariant = Red300Dark,
    onPrimary = Color.Black

)

enum class ColorPalette(
    val darkColors: Colors,
    val lightColors: Colors,
) {
    Orange(DarkOrangeColorPalette, LightOrangeColorPalette),
    Blue(DarkBlueColorPalette, LightBlueColorPalette),
    Purple(DarkPurpleColorPalette, LightPurpleColorPalette),
}