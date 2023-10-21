package com.mls.kmp.mor.nytnewskmp

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.mls.kmp.mor.nytnewskmp.ui.settings.SettingsViewModel
import com.mls.kmp.mor.nytnewskmp.ui.settings.ThemeConfig
import org.koin.compose.koinInject

@Composable
fun AndroidApp() {
    val settingsViewModel: SettingsViewModel = koinInject()
    val settingsUiStateState by settingsViewModel.state.collectAsState()

    val darkTheme = when (settingsUiStateState.theme) {
        ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.DARK -> true
        ThemeConfig.LIGHT -> false
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                !darkTheme
        }
    }

    MyApp(darkTheme)

}