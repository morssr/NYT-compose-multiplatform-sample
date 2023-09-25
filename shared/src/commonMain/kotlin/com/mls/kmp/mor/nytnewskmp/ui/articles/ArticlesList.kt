package com.mls.kmp.mor.nytnewskmp.ui.articles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: List<ArticleUIModel> = emptyList(),
    onArticleClick: (ArticleUIModel) -> Unit = {},
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> }
) {
    LazyVerticalStaggeredGrid(
        modifier = modifier,
        columns = StaggeredGridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 16.dp,
    ) {
        items(articles) { article ->
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                ArticleItem(
                    article = article,
                    onArticleClick = onArticleClick,
                    onBookmarkClick = onBookmarkClick
                )
            }
        }
    }
}