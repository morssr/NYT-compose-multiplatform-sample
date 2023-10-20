package com.mls.kmp.mor.nytnewskmp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomThemeAttributes(
    val secondaryBackground: Color = Color.Unspecified,
)

val LocalCustomThemeAttributes = staticCompositionLocalOf { CustomThemeAttributes() }

val MaterialTheme.customThemeAttributes: CustomThemeAttributes
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomThemeAttributes.current

