package com.mls.kmp.mor.nytnewskmp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticleUIModel
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticlesList
import com.mls.kmp.mor.nytnewskmp.ui.articles.LoadingShimmerArticlesList
import com.mls.kmp.mor.nytnewskmp.ui.common.CustomCollapsingToolbarContainer
import com.mls.kmp.mor.nytnewskmp.ui.common.WebViewRoute
import com.mls.kmp.mor.nytnewskmp.ui.home.HomeTopAppBar
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestTabsRow
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestsBar
import com.mls.kmp.mor.nytnewskmp.ui.home.InterestsBottomSheetDialog
import com.mls.kmp.mor.nytnewskmp.ui.populars.PopularBarComponent
import com.mls.kmp.mor.nytnewskmp.ui.settings.DialogSelector
import com.mls.kmp.mor.nytnewskmp.ui.settings.SettingsDialogsPresenter
import com.mls.kmp.mor.nytnewskmp.ui.settings.SettingsViewModel
import com.mls.kmp.mor.nytnewskmp.utils.findRouteNavigator
import kotlinx.coroutines.launch


private const val COLLAPSING_TOOLBAR_HEIGHT = 280

class HomeScreenRoute : Screen {

    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val routeNavigator = LocalNavigator.currentOrThrow.findRouteNavigator()

        val viewModel = getScreenModel<HomeScreenViewModel>()
        val state by viewModel.state.collectAsState()

        val settingsViewModel = getScreenModel<SettingsViewModel>()
        val settingsState by settingsViewModel.state.collectAsState()

        val pagerState = rememberPagerState { state.topics.size }

        LaunchedEffect(pagerState) {
            // Collect from the a snapshotFlow reading the currentPage
            snapshotFlow { pagerState.currentPage }.collect { page ->
                // Do something with each page change, for example:
                // viewModel.sendPageSelectedEvent(page)
                viewModel.refreshCurrentTopic(state.topics[page])
            }
        }

        val appBarState = rememberTopAppBarState()

        val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(appBarState)

        // calculate the height of the collapsing toolbar based on the screen height
        val collapsingToolbarHeight by remember {
            derivedStateOf { COLLAPSING_TOOLBAR_HEIGHT.dp }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                CustomCollapsingToolbarContainer(
                    initialHeight = collapsingToolbarHeight,
                    scrollBehavior = scrollBehavior,
                    alphaAnimation = true,
                )
                {
                    // returns true when the toolbar is fully collapsed
                    val appBarFullyCollapsed by remember {
                        derivedStateOf { appBarState.collapsedFraction > 0.99f }
                    }

                    Column(
                        modifier = Modifier.then(
                            // Hide the toolbar when the collapsing toolbar is fully collapsed.
                            // Workaround to fix the issue of consuming the touch events when collapsed.
                            if (appBarFullyCollapsed) {
                                Modifier.requiredHeight(0.dp)
                            } else {
                                Modifier.requiredHeight(collapsingToolbarHeight)
                            }
                        )
                    ) {

                        HomeTopAppBar(
                            showMainMenu = settingsState.dialogSelector == DialogSelector.MainMenuDropdown,
                            onMenuClick = { settingsViewModel.showDialog(DialogSelector.MainMenuDropdown) },
                            onLogoClick = { settingsViewModel.showDialog(DialogSelector.AboutUs) },
                            onDismissMenu = { settingsViewModel.dismissDialog() },
                            onSettingClick = { settingsViewModel.showDialog(DialogSelector.Settings) },
                            onAboutUsClick = { settingsViewModel.showDialog(DialogSelector.AboutUs) },
                            onContactUsClick = { settingsViewModel.showDialog(DialogSelector.ContactUs) },
                        )

                        PopularBarComponent(
                            modifier = Modifier.weight(1f),
                            popularsFeedState = state.popularFeedState,
                            onPopularStoryClick = {
                                navigateToArticleWeb(routeNavigator, it.title, it.storyUrl)
                            },
                        )
                    }
                }
            }
        ) { paddingValues ->

            SettingsDialogsPresenter(
                state = settingsState,
                onShowDialogClick = { selector -> settingsViewModel.showDialog(selector) },
                onDismiss = { settingsViewModel.dismissDialog() },
                onThemeChanged = { settingsViewModel.onThemeChanged(it) },
            )

            HomeScreenContent(
                modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                topics = state.topics,
                articlesStateMap = state.feedsStates,
                onArticleClick = { article ->
                    navigateToArticleWeb(routeNavigator, article.title, article.storyUrl)
                },
                onTopicsChooserDialogDismiss = { updatedTopics ->
                    viewModel.updateTopics(updatedTopics)
                },
                onBookmarkClick = { id, bookmarked ->
                    viewModel.updateBookmarks(id, bookmarked)
                },
                pagerState = pagerState
            )
        }
    }

    private fun navigateToArticleWeb(
        routeNavigator: Navigator,
        title: String,
        articleUrl: String
    ) {
        routeNavigator.push(WebViewRoute(title, articleUrl))
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreenContent(
    topics: List<Topics>,
    articlesStateMap: Map<Topics, ArticlesFeedState>,
    pagerState: PagerState = rememberPagerState { topics.size },
    onArticleClick: (ArticleUIModel) -> Unit = {},
    onTopicsChooserDialogDismiss: (List<Topics>) -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {

    val coroutineScope = rememberCoroutineScope()
    var showInterestsSelectionDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        color = Color.Transparent,
    ) {

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
                            onBookmarkClick = onBookmarkClick,
                            onArticleClick = onArticleClick
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
