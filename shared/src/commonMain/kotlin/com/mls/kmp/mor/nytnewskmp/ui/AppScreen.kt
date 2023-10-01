package com.mls.kmp.mor.nytnewskmp.ui

import HomeTab
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.mls.kmp.mor.nytnewskmp.ui.bookmarks.BookmarksTabRoute
import com.mls.kmp.mor.nytnewskmp.ui.search.SearchTabRoute

class AppScreen : Screen {

    @OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
    @Composable
    override fun Content() {
        val windowSizeClass = calculateWindowSizeClass()
        val shouldShowNavigationBar by remember(windowSizeClass) {
            mutableStateOf(windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact)
        }

        TabNavigator(HomeTab) {
            Scaffold(
                content = { innerPadding ->
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(
                                    WindowInsetsSides.Horizontal,
                                ),
                            ),
                    ) {

                        if (!shouldShowNavigationBar) {
                            NytNavigationRail()
                        }
                        CurrentTab()
                    }
                },
                bottomBar = {
                    if (shouldShowNavigationBar) {
                        NytNavigationBar()
                    }
                }
            )
        }
    }
}

@Composable
private fun NytNavigationBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        TabNavigationBarItem(HomeTab)
        TabNavigationBarItem(SearchTabRoute)
        TabNavigationBarItem(BookmarksTabRoute)
    }
}

@Composable
private fun RowScope.TabNavigationBarItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current.key == tab.key
    NavigationBarItem(
        selected = selected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                imageVector = getIconForTab(tab, selected),
                contentDescription = tab.options.title
            )
        }
    )
}

@Composable
private fun NytNavigationRail() {
    NavigationRail {
        TabNavigationRailItem(HomeTab)
        TabNavigationRailItem(SearchTabRoute)
        TabNavigationRailItem(BookmarksTabRoute)
    }
}

@Composable
private fun TabNavigationRailItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current
    val selected = tabNavigator.current.key == tab.key
    NavigationRailItem(
        selected = selected,
        onClick = { tabNavigator.current = tab },
        icon = {
            Icon(
                imageVector = getIconForTab(tab, selected),
                contentDescription = tab.options.title
            )
        }
    )
}

private fun getIconForTab(tab: Tab, selected: Boolean): ImageVector {
    return when (tab) {
        HomeTab -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
        SearchTabRoute -> if (selected) Icons.Filled.Search else Icons.Outlined.Search
        BookmarksTabRoute -> if (selected) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks
        else -> throw IllegalArgumentException("Unknown tab $tab")
    }
}
