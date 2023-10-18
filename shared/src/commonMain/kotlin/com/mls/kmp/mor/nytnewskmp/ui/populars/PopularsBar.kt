@file:OptIn(ExperimentalFoundationApi::class)

package com.mls.kmp.mor.nytnewskmp.ui.populars

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.PopularFeedState
import com.mls.kmp.mor.nytnewskmp.ui.common.CustomHorizontalPager
import com.mls.kmp.mor.nytnewskmp.ui.common.ItemImage
import com.mls.kmp.mor.nytnewskmp.ui.common.shimmerBackground
import dev.icerock.moko.resources.compose.stringResource

private const val TAG = "PopularBar"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopularBarComponent(
    popularsFeedState: PopularFeedState = PopularFeedState.Loading,
    modifier: Modifier = Modifier,
    onPopularStoryClick: (item: PopularUiModel) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier) {

        // Calculate the item width by the available width
        val popularItemWidth by remember {
            derivedStateOf {
                calculateDynamicPopularItemWidth(
                    maxWidth
                )
            }
        }

        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                        )
                    ) {
                        append(stringResource(MR.strings.newest))
                    }
                    append(" ")
                    append(stringResource(MR.strings.reports))
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .padding(horizontal = 16.dp)
            )

            when (popularsFeedState) {
                is PopularFeedState.Loading -> {
                    CustomHorizontalPager(
                        rememberPagerState { 10 },
                        PageSize.Fixed(popularItemWidth)
                    ) {
                        ShimmerPopularItem()
                    }
                }

                is PopularFeedState.Success -> {
                    CustomHorizontalPager(
                        rememberPagerState { popularsFeedState.data.size },
                        PageSize.Fixed(popularItemWidth)
                    ) { pageIndex ->
                        PopularListItem(
                            modifier = Modifier.fillMaxSize(),
                            popular = popularsFeedState.data[pageIndex],
                            onItemClick = onPopularStoryClick
                        )
                    }
                }

                is PopularFeedState.Error -> {
                    Text(
                        text = popularsFeedState.error.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun PopularListItem(
    modifier: Modifier = Modifier,
    popular: PopularUiModel,
    onItemClick: (item: PopularUiModel) -> Unit = {}
) {
    Box(modifier = modifier.clickable { onItemClick(popular) }) {

        ItemImage(
            modifier = Modifier.fillMaxSize(),
            url = popular.imageUrl,
            contentDescription = stringResource(MR.strings.popular_article_image_content_description)
        )

        Surface(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = Color.Transparent
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.61f)),
            ) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    text = popular.title,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start,
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ShimmerPopularItem(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize().shimmerBackground())
}

private fun calculateDynamicPopularItemWidth(containerWidth: Dp): Dp = when {
    containerWidth >= 800.dp -> containerWidth * 0.53f
    containerWidth >= 600.dp -> containerWidth * 0.61f
    else -> containerWidth * 0.8f
}