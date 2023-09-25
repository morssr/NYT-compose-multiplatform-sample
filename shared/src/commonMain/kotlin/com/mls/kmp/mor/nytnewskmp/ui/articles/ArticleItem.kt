package com.mls.kmp.mor.nytnewskmp.ui.articles

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.rounded.BookmarkRemove
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.common.ExpandableText
import com.mls.kmp.mor.nytnewskmp.ui.common.ItemImage
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ArticleItem(
    modifier: Modifier = Modifier,
    article: ArticleUIModel,
    onBookmarkClick: (String, Boolean) -> Unit = { _, _ -> },
    onArticleClick: (ArticleUIModel) -> Unit = {}
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(50))
            .clickable {
                onArticleClick(article)
            },

        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {

        ItemImage(
            url = article.imageUrl,
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = article.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
                    .animateContentSize(animationSpec = tween(50))
            )

            IconToggleButton(
                modifier = Modifier.align(Alignment.Top),
                checked = article.favorite,
                onCheckedChange = { onBookmarkClick(article.id, article.favorite) }
            ) {
                if (article.favorite) {
                    Icon(
                        Icons.Rounded.BookmarkRemove,
                        contentDescription = stringResource(MR.strings.add_article_to_bookmarks)
                    )
                } else {
                    Icon(
                        Icons.Outlined.BookmarkAdd,
                        contentDescription = stringResource(MR.strings.remove_article_from_bookmarks)
                    )
                }
            }
        }

        ExpandableText(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .paddingFromBaseline(top = 24.dp, bottom = 16.dp),
            collapsedMaxLine = 3,
            text = article.content,
            showMoreText = stringResource(MR.strings.expand_text_more),
            onUnAnnotatedTextClick = { onArticleClick }
        )
    }
}