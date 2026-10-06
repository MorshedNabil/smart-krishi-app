package com.nabil.smartkrishi.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
        val view = LocalView.current

        SideEffect {
            val window = view.context.findActivity()?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, view).apply {
                    // to get light color system bar icons use false and to get dark color system bar icons use true
                    isAppearanceLightStatusBars = true
                    isAppearanceLightNavigationBars = true
                }
            }
        }
        Surface(
            color = colorScheme.background,
            content = content
        )
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}