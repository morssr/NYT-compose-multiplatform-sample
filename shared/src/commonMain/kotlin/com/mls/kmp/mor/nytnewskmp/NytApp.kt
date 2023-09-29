package com.mls.kmp.mor.nytnewskmp

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.mls.kmp.mor.nytnewskmp.ui.AppScreen
import com.mls.kmp.mor.nytnewskmp.ui.theme.MyAppTheme

@Composable
fun MyApp(darkTheme: Boolean = isSystemInDarkTheme()) {
    MyAppTheme(darkTheme) {
        Navigator(AppScreen())
    }
}