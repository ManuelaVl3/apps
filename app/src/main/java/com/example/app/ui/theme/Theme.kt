package com.example.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.app.R

val MontserratFamily = FontFamily(
    Font(R.font.montserrat_thin, FontWeight.Thin),
    Font(R.font.montserrat_extralight, FontWeight.ExtraLight),
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold),
    Font(R.font.montserrat_extrabold, FontWeight.ExtraBold),
    Font(R.font.montserrat_black, FontWeight.Black)
)

val BgLight    = Color(0xFFFFFFFF)
val SurfaceLight = Color(0xFFFFFFFF)
val BgDark     = Color(0xFF121212)
val SurfaceDark= Color(0xFF1E1E1E)
val TextLightOnDark = Color(0xFFF5F5F5)

private val DarkColorScheme = darkColorScheme(
    primary = OrangeDeep,
    secondary = Orange,
    tertiary = Peach,
    background = BgDark,
    surface = SurfaceDark,
    onBackground = TextLightOnDark,
    onSurface = TextLightOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeDeep,
    secondary = Orange,
    tertiary = Peach,
    background = BgLight,
    surface = SurfaceLight,
    onBackground = TextDark,
    onSurface = TextDark
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
