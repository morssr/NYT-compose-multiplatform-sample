package com.mls.kmp.mor.nytnewskmp.ui.search

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

object SearchTabRoute : Tab {

    override val options: TabOptions
        @Composable
        get() {
            val title = "Search"
            val icon = rememberVectorPainter(Icons.Rounded.Search)
            return remember {
                TabOptions(
                    index = 1u,
                    title = title,
                    icon = icon,
                )
            }
        }

    @Composable
    override fun Content() {
        Navigator(IdleSearchScreenRoute())
    }
}