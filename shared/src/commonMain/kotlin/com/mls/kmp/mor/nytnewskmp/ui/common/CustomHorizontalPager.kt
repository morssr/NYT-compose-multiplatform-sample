package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomHorizontalPager(
    pagerState: PagerState,
    pageSize: PageSize,
    item: @Composable (pageIndex: Int) -> Unit
) {
    HorizontalPager(
        state = pagerState,
        pageSize = pageSize,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
    ) { pageIndex ->
        Card(Modifier.graphicsLayer {
            // Calculate the absolute offset for the current page from the
            // scroll position. We use the absolute value which allows us to mirror
            // any effects for both directions
            val pageOffset = (
                    (pagerState.currentPage - pageIndex) + pagerState
                        .currentPageOffsetFraction
                    ).absoluteValue

            // animate the alpha, between 50% and 100%
            alpha = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            // animate the scaleX + scaleY, between 85% and 100%
            val minScale = 0.85f

            scaleX = lerp(
                start = 0.85f,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )

            scaleY = lerp(
                start = minScale,
                stop = 1f,
                fraction = 1f - pageOffset.coerceIn(0f, 1f)
            )
        }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                // Card content
                item(pageIndex)
            }
        }
    }
}