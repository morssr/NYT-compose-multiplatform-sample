package com.mls.kmp.mor.nytnewskmp.ui.bookmarks

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object BookmarksTabRoute : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Bookmarks"
            val icon = rememberVectorPainter(Icons.Rounded.Bookmarks)
            return remember {
                TabOptions(
                    index = 2u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(BookmarksScreenRoute())
    }
}