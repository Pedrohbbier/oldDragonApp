package com.olddragon.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DragonGold,
    secondary = DragonSilver,
    tertiary = MedievalBlue,
    background = DragonBlack,
    surface = InkBlack,
    onPrimary = DragonBlack,
    onSecondary = DragonBlack,
    onTertiary = Parchment,
    onBackground = Parchment,
    onSurface = Parchment
)

private val LightColorScheme = lightColorScheme(
    primary = DragonGold,
    secondary = DragonRed,
    tertiary = MedievalBlue,
    background = Parchment,
    surface = Parchment,
    onPrimary = DragonBlack,
    onSecondary = Parchment,
    onTertiary = Parchment,
    onBackground = InkBlack,
    onSurface = InkBlack
)

@Composable
fun OldDragonTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}