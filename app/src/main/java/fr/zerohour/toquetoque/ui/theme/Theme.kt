package fr.zerohour.toquetoque.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun ToqueToqueTheme(
    themeMode: Int = 0, // 0 = Auto, 1 = Light, 2 = Dark
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit

) {
    val darkTheme = when (themeMode) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> ToqueToqueDarkColorScheme
        else -> ToqueToqueColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ToqueToqueTypography,
        shapes = ToqueToqueShapes,
        content = content
    )
}