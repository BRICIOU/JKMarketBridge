package zm.edu.justinkabwe.jkmarketbridge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GreenColorScheme = lightColorScheme(
    primary = GreenPrimary,
    onPrimary = GreenOnPrimary,
    primaryContainer = GreenPrimaryContainer,
    secondary = GreenSecondary,
    secondaryContainer = GreenSecondaryContainer,
    onSecondaryContainer = GreenOnSecondaryContainer,
    tertiary = GreenTertiary,
    tertiaryContainer = GreenTertiaryContainer,
    background = GreenBackground,
    surface = GreenSurface,
    surfaceVariant = GreenSurfaceVariant,
    onSurface = GreenOnSurface,
    onSurfaceVariant = GreenOnSurfaceVariant,
    outline = GreenOutline,
)

@Composable
fun JKMarketBridgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GreenColorScheme,
        typography = Typography,
        content = content
    )
}