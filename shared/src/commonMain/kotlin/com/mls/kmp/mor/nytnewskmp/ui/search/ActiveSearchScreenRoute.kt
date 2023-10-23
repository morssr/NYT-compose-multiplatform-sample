package com.mls.kmp.mor.nytnewskmp.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.unit.dp
import app.cash.paging.LoadStateError
import app.cash.paging.LoadStateLoading
import app.cash.paging.LoadStateNotLoading
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.common.GradientBackgroundContainer
import com.mls.kmp.mor.nytnewskmp.ui.common.ItemImage
import com.mls.kmp.mor.nytnewskmp.ui.common.WebViewRoute
import com.mohamedrejeb.calf.ui.progress.AdaptiveCircularProgressIndicator
import dev.icerock.moko.resources.compose.stringResource

class ActiveSearchScreenRoute : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<SearchViewModel>()
        val state by viewModel.state.collectAsState()

        val searchResult = state.searchResults.collectAsLazyPagingItems()

        SearchScreenContent(
            searchResult,
            onStartSearchClick = { query -> viewModel.searchStories(query) },
            onNavigateBack = { navigator.pop() },
            onBookmarkClick = { searchUiModel -> viewModel.bookmarkArticle(searchUiModel) },
            onArticleClick = { searchUiModel ->
                navigator.push(WebViewRoute(searchUiModel.title, searchUiModel.articleUrl))
            }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    searchResult: LazyPagingItems<SearchUiModel>,
    onStartSearchClick: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {},
    onArticleClick: (SearchUiModel) -> Unit = {},
    onBookmarkClick: (SearchUiModel) -> Unit = {},
) {
    var query by rememberSaveable { mutableStateOf("") }
    val imeController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var isSearchTextFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        Modifier.fillMaxSize()
    ) {

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .paddingFromBaseline(top = 64.dp)
                .onFocusChanged { isSearchTextFieldFocused = it.isFocused }
                .focusRequester(focusRequester),
            query = query,
            active = true,
            onActiveChange = { },
            onQueryChange = { query = it },
            onSearch = {
                clearFocus(focusManager, imeController)
                onStartSearchClick(query)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(MR.strings.back),
                    modifier = Modifier.clickable {
                        onNavigateBack()
                    })
            },
            trailingIcon = {
                AnimatedVisibility(visible = query.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .scale(0.75f)
                            .clickable { query = "" },
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(MR.strings.clear)
                    )
                }
            },
            placeholder = { Text(text = stringResource(MR.strings.search_for_something_message)) },
            colors = SearchBarDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            GradientBackgroundContainer {
                SearchResults(
                    searchResult,
                    onArticleClick = onArticleClick,
                    onBookmarkClick = onBookmarkClick
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private fun clearFocus(
    focusManager: FocusManager,
    imeController: SoftwareKeyboardController?
) {
    focusManager.clearFocus()
    imeController?.hide()
}

@Composable
private fun SearchResults(
    articles: LazyPagingItems<SearchUiModel>,
    onArticleClick: (SearchUiModel) -> Unit = {},
    onBookmarkClick: (SearchUiModel) -> Unit = {},
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp
    ) {
        when (val loadState = articles.loadState.refresh) {
            LoadStateLoading -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Box(Modifier.fillMaxSize()) {
                        AdaptiveCircularProgressIndicator(
                            Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            is LoadStateNotLoading -> {
                items(articles.itemCount) { index ->
                    val article = articles[index]
                    SearchArticleCell(
                        article = article!!,
                        onBookmarkClick = onBookmarkClick,
                        onArticleClick = onArticleClick
                    )
                }
            }

            is LoadStateError -> {
                item {
                    Text(loadState.error.message!!)
                }
            }

            else -> error("when should be exhaustive")
        }
    }
}

@Composable
fun SearchArticleCell(
    modifier: Modifier = Modifier,
    article: SearchUiModel,
    onBookmarkClick: (SearchUiModel) -> Unit = {},
    onArticleClick: (SearchUiModel) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onArticleClick(article)
            },

        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {

        ItemImage(
            url = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = article.title,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 24.dp, bottom = 16.dp),
            text = article.content
        )

        Box(Modifier.fillMaxWidth()) {

            IconToggleButton(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.CenterEnd),
                checked = false,
                onCheckedChange = { onBookmarkClick(article) }
            ) {
                Icon(
                    Icons.Outlined.BookmarkAdd,
                    contentDescription = stringResource(MR.strings.remove_article_from_bookmarks)
                )
            }
        }
    }
}