package com.mls.kmp.mor.nytnewskmp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticlesList
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

class HomeScreenRoute : Screen {

    @Composable
    override fun Content() {
        val viewModel: HomeScreenViewModel = koinInject()
        val state by viewModel.state.collectAsState()

        HomeScreenContent(state = state)
    }
}

@Composable
fun HomeScreenContent(
    state: HomeScreenState,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {
            when (state) {
                is HomeScreenState.Loading -> Text(text = "Loading...")
                is HomeScreenState.Error -> Text(text = "Error!")
                is HomeScreenState.Success -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        ArticlesList(
                            articles = state.data,
                            modifier = Modifier.fillMaxSize(),
                            onBookmarkClick = { _, _ -> },
                            onArticleClick = {}
                        )
                    }
                }
            }
        }
    }
}