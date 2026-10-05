package com.nabil.smartkrishi.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val LightColors = lightColorScheme(
    primary = GreenPrimary,
    secondary = YellowAccent,
    background = BackgroundCream,
    surface = Surface
)

@Composable
fun SmartKrishiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            content = content
        )
    }
}