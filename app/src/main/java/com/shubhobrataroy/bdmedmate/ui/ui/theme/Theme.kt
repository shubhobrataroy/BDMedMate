package com.shubhobrataroy.bdmedmate.ui.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Color.LightGray,
    onSurface = Color.White,
    onSecondary = Color.White,
)

private val LightColorPalette = lightColors(
    primaryVariant = Purple700,
    secondary = Color.DarkGray,
    secondaryVariant = Color.DarkGray,
    onSurface = Color.Black,
    onSecondary = Color.White,
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)
var CurrentColorPalette = DarkColorPalette

@Composable
fun MedMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CurrentColorPalette = colors

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}