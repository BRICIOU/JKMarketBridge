package zm.edu.justinkabwe.jkmarketbridge.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val EnterpriseColorScheme = lightColorScheme(
    primary                = NavyPrimary,
    onPrimary              = NavyOnPrimary,
    primaryContainer       = NavyPrimaryContainer,
    onPrimaryContainer     = NavyOnPrimaryContainer,
    secondary              = GreenVerified,
    onSecondary            = GreenOnVerified,
    secondaryContainer     = GreenVerifiedContainer,
    onSecondaryContainer   = GreenOnVerifiedContainer,
    tertiary               = AmberTrade,
    onTertiary             = AmberOnTrade,
    tertiaryContainer      = AmberTradeContainer,
    onTertiaryContainer    = AmberOnTradeContainer,
    background             = EnterpriseBg,
    onBackground           = InkDark,
    surface                = EnterpriseSurface,
    onSurface              = InkDark,
    surfaceVariant         = EnterpriseSurfaceVar,
    onSurfaceVariant       = InkLight,
    outline                = OutlineGr,
    error                  = ErrorRed,
)

@Composable
fun JKMarketBridgeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EnterpriseColorScheme,
        typography = Typography,
        content = content
    )
}