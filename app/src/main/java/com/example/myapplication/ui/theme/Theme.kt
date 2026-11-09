package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Terracotta,
    onPrimary = WhitePure,
    secondary = GrayText,
    background = CreamLight,
    surface = CardBackground,
    onBackground = CharcoalDark,
    onSurface = CharcoalDark
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
