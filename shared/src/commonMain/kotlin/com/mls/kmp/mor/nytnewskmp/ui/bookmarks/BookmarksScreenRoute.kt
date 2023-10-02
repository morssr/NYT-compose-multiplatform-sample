package com.mls.kmp.mor.nytnewskmp.ui.bookmarks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.common.WebViewRoute
import com.mls.kmp.mor.nytnewskmp.utils.findRouteNavigator
import dev.icerock.moko.resources.compose.stringResource

class BookmarksScreenRoute : Screen {

    @Composable
    override fun Content() {
        val routeNavigator = LocalNavigator.currentOrThrow.findRouteNavigator()

        val viewModel = getScreenModel<BookmarksScreenViewModel>()
        val state = viewModel.state.collectAsState().value

        BookmarksScreenContent(
            articles = state.bookmarks,
            onArticleClick = { article ->
                navigateToArticleWeb(routeNavigator, article)
            },
            onArticleDelete = { id ->
                viewModel.deleteBookmark(id)
            }
        )
    }

    private fun navigateToArticleWeb(
        routeNavigator: Navigator,
        article: BookmarkUiModel
    ) {
        routeNavigator.push(WebViewRoute(article.title, article.articleUrl))
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BookmarksScreenContent(
    modifier: Modifier = Modifier,
    articles: List<BookmarkUiModel>,
    lazyListState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    onArticleClick: (article: BookmarkUiModel) -> Unit = {},
    onArticleDelete: (id: String) -> Unit = {},
) {

    if (articles.isEmpty()) {
        BookmarksEmptyScreen()
        return
    }

    LazyVerticalStaggeredGrid(
        state = lazyListState,
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {

        item(span = StaggeredGridItemSpan.FullLine) {
            Column {
                Text(
                    modifier = Modifier
                        .paddingFromBaseline(top = 32.dp)
                        .padding(horizontal = 8.dp),
                    text = stringResource(MR.strings.bookmarks_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(
            items = articles,
            key = { article -> article.id }
        ) { article ->

            val dismissState = rememberDismissState(
                confirmValueChange = {
                    if (it == DismissValue.DismissedToStart) {
                        onArticleDelete(article.id)
                    }
                    true
                },
                positionalThreshold = { 128.dp.toPx() }
            )
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                SwipeToDeleteBookmarkItem(
                    modifier = Modifier.animateItemPlacement(),
                    bookmarked = article,
                    dismissState = dismissState,
                    onArticleClick = onArticleClick
                )
            }
        }
    }
}

@Composable
fun BookmarksEmptyScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Bookmarks Empty", Modifier.align(Alignment.Center))
    }
}