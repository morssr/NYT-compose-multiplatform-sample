package com.mls.kmp.mor.nytnewskmp.ui.bookmarks

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.ui.common.ItemImage
import com.mls.kmp.mor.nytnewskmp.ui.common.SwipeToDeleteBackground

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookmarkItem(
    modifier: Modifier = Modifier,
    bookmarkedStory: BookmarkUiModel,
    onStoryClick: (article: BookmarkUiModel) -> Unit = {},
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(50))
            .clickable { onStoryClick(bookmarkedStory) },
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Column {
            ItemImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                url = bookmarkedStory.imageUrl,
                contentDescription = bookmarkedStory.title
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .paddingFromBaseline(top = 20.dp)
                    .alpha(0.8f),
                text = bookmarkedStory.subsection.ifEmpty { bookmarkedStory.topic },
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelSmall
            )

            Text(
                modifier = Modifier
                    .paddingFromBaseline(top = 24.dp)
                    .padding(horizontal = 16.dp),
                text = bookmarkedStory.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleLarge
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(0.7f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .paddingFromBaseline(16.dp),
                    text = bookmarkedStory.byline,
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .paddingFromBaseline(16.dp),
                    text = bookmarkedStory.publishedDate,
                    style = MaterialTheme.typography.labelSmall
                )

            }
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .paddingFromBaseline(24.dp),
                text = bookmarkedStory.content
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteBookmarkItem(
    bookmarked: BookmarkUiModel,
    swipeToDismissBoxState: SwipeToDismissBoxState = rememberSwipeToDismissBoxState(),
    modifier: Modifier = Modifier,
    onArticleClick: (article: BookmarkUiModel) -> Unit = {},
) {
    SwipeToDismissBox(
        modifier = modifier,
        state = swipeToDismissBoxState,
        backgroundContent = {
            if (swipeToDismissBoxState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                SwipeToDeleteBackground(swipeToDismissBoxState)
            }
        }
    ) {
        BookmarkItem(
            modifier = modifier
                .fillMaxWidth()
                .alpha(if (swipeToDismissBoxState.progress <= 0.9) 1f - swipeToDismissBoxState.progress else 1f),
            bookmarkedStory = bookmarked,
            onStoryClick = onArticleClick
        )
    }
}