package fr.zerohour.toquetoque.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Primaire
val PrimaryMint = Color(0xFF006D37)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFF2ECC71)
val OnPrimaryContainer = Color(0xFF005027)
val InversePrimary = Color(0xFF4AE183)

// Secondaire
val SecondarySlate = Color(0xFF4E6073)
val OnSecondary = Color(0xFFFFFFFF)
val SecondaryContainer = Color(0xFFCFE2F9)
val OnSecondaryContainer = Color(0xFF526478)

// Tertiaire
val TertiarySlate = Color(0xFF4B6076)
val OnTertiary = Color(0xFFFFFFFF)
val TertiaryContainer = Color(0xFF9FB5CE)
val OnTertiaryContainer = Color(0xFF32475C)

// Surfaces et arrière-plans
val SurfaceBackground = Color(0xFFF7F9FB)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF2F4F6)
val SurfaceContainer = Color(0xFFECEEF0)
val SurfaceContainerHigh = Color(0xFFE6E8EA)
val SurfaceContainerHighest = Color(0xFFE0E3E5)
val OnSurface = Color(0xFF191C1E)
val OnSurfaceVariant = Color(0xFF3D4A3E)

// Contours et erreurs
val OutlineMint = Color(0xFF6C7B6D)
val OutlineVariant = Color(0xFFBBCBBB)
val ErrorRed = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)
val OnErrorContainer = Color(0xFF93000A)

// Thème clair
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

// Primaire
val PrimaryMintDark = Color(0xFF4AE183)
val OnPrimaryDark = Color(0xFF00391A)
val PrimaryContainerDark = Color(0xFF005228)
val OnPrimaryContainerDark = Color(0xFF6CFD9D)

// Secondaire
val SecondarySlateDark = Color(0xFFB3C8E0)
val OnSecondaryDark = Color(0xFF1E3244)
val SecondaryContainerDark = Color(0xFF35485B)
val OnSecondaryContainerDark = Color(0xFFCFE2F9)

// Tertiaire
val TertiarySlateDark = Color(0xFFAEC4DD)
val OnTertiaryDark = Color(0xFF1A324A)
val TertiaryContainerDark = Color(0xFF324961)
val OnTertiaryContainerDark = Color(0xFFCADFF9)

// Surfaces et arrière-plans
val SurfaceBackgroundDark = Color(0xFF111416)
val SurfaceContainerHighestDark = Color(0xFF323537)
val OnSurfaceDark = Color(0xFFE2E2E2)
val OnSurfaceVariantDark = Color(0xFFC0C9C0)

// Contours et erreurs
val OutlineMintDark = Color(0xFF8B9B8B)
val OutlineVariantDark = Color(0xFF425042)

// Thème Sombre

val ToqueToqueDarkColorScheme = darkColorScheme(
    primary = PrimaryMintDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    inversePrimary = PrimaryMint,

    secondary = SecondarySlateDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,

    tertiary = TertiarySlateDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,

    background = SurfaceBackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceBackgroundDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceContainerHighestDark,
    onSurfaceVariant = OnSurfaceVariantDark,

    outline = OutlineMintDark,
    outlineVariant = OutlineVariantDark,

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)