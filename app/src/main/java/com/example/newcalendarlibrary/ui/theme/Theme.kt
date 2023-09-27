package com.example.newcalendarlibrary.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// Define a dark color palette using specific colors for primary, primary variant, and secondary.
private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

// Define a light color palette using specific colors for primary, primary variant, and secondary.
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

// Composable function to apply the theme based on the system's light/dark mode preference.
@Composable
fun NewCalendarLibraryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Select the appropriate color palette based on the darkTheme parameter.
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    // Apply MaterialTheme using the chosen color palette, typography, shapes, and provided content.
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}