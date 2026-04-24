package com.example.family_attendence_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Teal600  = Color(0xFF0D9488)
val Teal100  = Color(0xFFE1F5EE)
val Amber600 = Color(0xFFBA7517)
val Amber100 = Color(0xFFFAEEDA)
val Navy     = Color(0xFF1A2E4A)

private val Light = lightColorScheme(
    primary             = Teal600,
    onPrimary           = Color.White,
    primaryContainer    = Teal100,
    onPrimaryContainer  = Color(0xFF085041),
    secondary           = Amber600,
    onSecondary         = Color.White,
    secondaryContainer  = Amber100,
    onSecondaryContainer= Color(0xFF633806),
    background          = Color(0xFFF6FAF9),
    surface             = Color.White,
    onBackground        = Navy,
    onSurface           = Navy,
    surfaceVariant      = Color(0xFFEFF6F4),
    onSurfaceVariant    = Color(0xFF4A6360),
    error               = Color(0xFFA32D2D),
    errorContainer      = Color(0xFFFCEBEB),
    outline             = Color(0xFFB2CECA)
)

@Composable
fun FamilyAttendanceTheme(
    dark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = Light,
        content     = content
    )
}