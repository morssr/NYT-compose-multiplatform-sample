package com.mls.kmp.mor.nytnewskmp.ui

import HomeTab
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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

    @Composable
    override fun Content() {
        TabNavigator(HomeTab) {
            Scaffold(
                content = {
                    Box(modifier = Modifier.padding(it)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    NytNavigationBar()
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
        TabNavigationItem(HomeTab)
        TabNavigationItem(SearchTabRoute)
        TabNavigationItem(BookmarksTabRoute)
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
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

private fun getIconForTab(tab: Tab, selected: Boolean): ImageVector {
    return when (tab) {
        HomeTab -> if (selected) Icons.Filled.Home else Icons.Outlined.Home
        SearchTabRoute -> if (selected) Icons.Filled.Search else Icons.Outlined.Search
        BookmarksTabRoute -> if (selected) Icons.Filled.Bookmarks else Icons.Outlined.Bookmarks
        else -> throw IllegalArgumentException("Unknown tab $tab")
    }
}
