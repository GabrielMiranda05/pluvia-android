package br.com.pluvia.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = WhiteSurface,
    primaryContainer = BlueSurface,
    onPrimaryContainer = BlueDark,
    secondary = BlueAccent,
    onSecondary = WhiteSurface,
    background = BlueBackground,
    onBackground = BlueDark,
    surface = WhiteSurface,
    onSurface = BlueDark,
    surfaceVariant = BlueSurface,
    outline = GrayBorder
)

@Composable
fun PluviaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}

// Alias de retrocompatibilidade — mantém compilação de quem ainda chama PluviaTheme
@Composable
fun PluviaTheme(content: @Composable () -> Unit) = PluviaTheme(content)
