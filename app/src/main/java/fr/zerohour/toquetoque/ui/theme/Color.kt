package fr.zerohour.toquetoque.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primary & Mint Base
val PrimaryMint = Color(0xFF006D37)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF2ECC71)
val OnPrimaryContainer = Color(0xFF005027)
val InversePrimary = Color(0xFF4AE183)

// Secondary & Slate Base
val SecondarySlate = Color(0xFF4E6073)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFCFE2F9)
val OnSecondaryContainer = Color(0xFF526478)

// Tertiary Base
val TertiarySlate = Color(0xFF4B6076)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF9FB5CE)
val OnTertiaryContainer = Color(0xFF32475C)

// Surface & Backgrounds
val SurfaceBackground = Color(0xFFF7F9FB)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF2F4F6)
val SurfaceContainer = Color(0xFFECEEF0)
val SurfaceContainerHigh = Color(0xFFE6E8EA)
val SurfaceContainerHighest = Color(0xFFE0E3E5)
val OnSurface = Color(0xFF191C1E)
val OnSurfaceVariant = Color(0xFF3D4A3E)

// Outlines & Errors
val OutlineMint = Color(0xFF6C7B6D)
val OutlineVariant = Color(0xFFBBCBBB)
val ErrorRed = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

// The Light Theme Color Scheme
val ToqueToqueColorScheme = lightColorScheme(
    primary = PrimaryMint,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,

    secondary = SecondarySlate,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = TertiarySlate,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    background = SurfaceBackground,
    onBackground = OnSurface,
    surface = SurfaceBackground,
    onSurface = OnSurface,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariant,

    outline = OutlineMint,
    outlineVariant = OutlineVariant,

    error = ErrorRed,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer
)