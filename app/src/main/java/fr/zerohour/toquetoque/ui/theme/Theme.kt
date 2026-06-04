package fr.zerohour.toquetoque.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ToqueToqueTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ToqueToqueColorScheme,
        typography = ToqueToqueTypography,
        shapes = ToqueToqueShapes,
        content = content
    )
}