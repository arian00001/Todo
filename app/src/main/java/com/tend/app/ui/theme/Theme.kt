package com.tend.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object TendColors {
    val Ink = Color(0xFF1B1A28)
    val InkSoft = Color(0xFF5B5872)
    val Ivory = Color(0xFFFAF7F2)
    val Indigo = Color(0xFF5B4BD6)
    val IndigoSoft = Color(0xFFEDEAFF)
    val Rose = Color(0xFFE11D48)
    val Amber = Color(0xFFF59E0B)
    val Emerald = Color(0xFF10B981)
    val GlassTop = Color(0xE6FFFFFF)
    val GlassBottom = Color(0xB3FFFFFF)
    val GlassBorder = Color(0x66FFFFFF)
}

private val LightScheme = lightColorScheme(
    primary = TendColors.Indigo,
    onPrimary = Color.White,
    secondary = TendColors.IndigoSoft,
    onSecondary = Color(0xFF3B2FB0),
    background = TendColors.Ivory,
    onBackground = TendColors.Ink,
    surface = Color.White,
    onSurface = TendColors.Ink,
    surfaceVariant = Color(0xFFF1EDE6),
    onSurfaceVariant = TendColors.InkSoft,
    outline = Color(0xFFE3DED4),
    error = TendColors.Rose
)

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF9C8FFF),
    onPrimary = Color(0xFF171233),
    secondary = Color(0xFF2B2745),
    onSecondary = Color(0xFFD8D2FF),
    background = Color(0xFF111019),
    onBackground = Color(0xFFF3F0EA),
    surface = Color(0xFF1A1826),
    onSurface = Color(0xFFF3F0EA),
    surfaceVariant = Color(0xFF242135),
    onSurfaceVariant = Color(0xFFB6B1C6),
    outline = Color(0xFF3A3650),
    error = Color(0xFFFF6B8A)
)

val TendTypography = Typography(
    displaySmall = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Normal, fontSize = 40.sp, lineHeight = 44.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Normal, fontSize = 30.sp, lineHeight = 34.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Normal, fontSize = 24.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 15.sp, lineHeight = 22.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp, lineHeight = 20.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp, lineHeight = 16.sp),
    labelSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 1.5.sp)
)

val TendShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun TendTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = TendTypography,
        shapes = TendShapes,
        content = content
    )
}
