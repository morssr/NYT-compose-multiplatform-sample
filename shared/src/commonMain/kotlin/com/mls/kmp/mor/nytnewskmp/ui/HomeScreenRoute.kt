package com.mls.kmp.mor.nytnewskmp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticlesList
import com.mls.kmp.mor.nytnewskmp.ui.articles.LoadingShimmerArticlesList
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestTabsRow
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestsBar
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestsBottomSheetDialog
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class HomeScreenRoute : Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel: HomeScreenViewModel = koinInject()
        val state by viewModel.state.collectAsState()

        val pagerState = rememberPagerState { state.topics.size }

        LaunchedEffect(pagerState) {
            // Collect from the a snapshotFlow reading the currentPage
            snapshotFlow { pagerState.currentPage }.collect { page ->
                // Do something with each page change, for example:
                // viewModel.sendPageSelectedEvent(page)
                viewModel.refreshCurrentTopic(state.topics[page])
            }
        }

        HomeScreenContent(
            topics = state.topics,
            articlesStateMap = state.feedsStates,
            onTopicsChooserDialogDismiss = { updatedTopics ->
                viewModel.updateTopics(updatedTopics)
            },
            pagerState = pagerState
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    topics: List<Topics>,
    articlesStateMap: Map<Topics, ArticlesFeedState>,
    pagerState: PagerState = rememberPagerState { topics.size },
    onTopicsChooserDialogDismiss: (List<Topics>) -> Unit = {},
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    var showInterestsSelectionDialog by remember { mutableStateOf(false) }

    Surface(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing)) {

            if (showInterestsSelectionDialog) {
                InterestsBottomSheetDialog(
                    selectedTopics = topics,
                    onDismiss = { updated, topics ->
                        if (updated) {
                            coroutineScope.launch { pagerState.animateScrollToPage(0) }
                        }
                        onTopicsChooserDialogDismiss(topics)
                        showInterestsSelectionDialog = false
                    },
                )
            }

            Column(modifier = Modifier) {

                InterestsBar(onShowTopicsSelectionDialog = { showInterestsSelectionDialog = true })

                InterestTabsRow(
                    topics = topics,
                    modifier = Modifier.padding(bottom = 8.dp),
                    currentSelectedPageIndex = pagerState.currentPage,
                    onTabClick = { page ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    },
                )

                HorizontalPager(
                    state = pagerState,
                    pageSpacing = 16.dp,
                    //TODO test the effect
//                    beyondBoundsPageCount = 1
                ) {

                    when (val feedState = articlesStateMap[topics[it]]) {
                        is ArticlesFeedState.Loading -> {
                            LoadingShimmerArticlesList()
                        }

                        is ArticlesFeedState.Error -> {
                            Text(text = "Error ${feedState.error}")
                        }

                        is ArticlesFeedState.Success -> {
                            ArticlesList(
                                articles = feedState.data,
                                modifier = Modifier.fillMaxSize(),
                                onBookmarkClick = { _, _ -> },
                                onArticleClick = {}
                            )
                        }

                        //TODO check null is needed
                        null -> {
                            println("null")
                        }
                    }
                }
            }
        }
    }
}
