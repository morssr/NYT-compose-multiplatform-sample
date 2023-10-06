package com.mls.kmp.mor.nytnewskmp.ui.search

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.ui.common.ItemImage

@Composable
fun HorizontalSearchItemsListElement(
    modifier: Modifier = Modifier,
    title: String,
    searchUiItems: List<SearchUiModel> = emptyList(),
    lazyListState: LazyListState = rememberLazyListState(),
    onSearchItemClick: (SearchUiModel) -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            items(searchUiItems) { it ->
                SmallSearchStoryItem(
                    story = it,
                    onStoryClick = { onSearchItemClick(it) }
                )
            }
        }
    }
}

@Composable
fun SmallSearchStoryItem(
    modifier: Modifier = Modifier,
    story: SearchUiModel,
    onStoryClick: (SearchUiModel) -> Unit = {}
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        ElevatedCard(
            modifier = modifier
                .width(300.dp)
                .animateContentSize(animationSpec = tween(50))
                .clickable {
                    onStoryClick(story)
                },
        ) {
            Box(modifier = Modifier) {
                ItemImage(
                    modifier = Modifier.aspectRatio(4f / 3f),
                    url = story.imageUrl,
                    contentDescription = story.title
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    color = Color.Black.copy(alpha = 0.61f)
                ) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp), text = story.title,
                        minLines = 2,
                        maxLines = 2,
                        color = Color.White,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}