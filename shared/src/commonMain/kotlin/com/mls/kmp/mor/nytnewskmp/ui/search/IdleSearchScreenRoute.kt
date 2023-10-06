package com.mls.kmp.mor.nytnewskmp.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.common.WebViewRoute
import com.mls.kmp.mor.nytnewskmp.utils.findRouteNavigator
import dev.icerock.moko.resources.compose.stringResource

class IdleSearchScreenRoute : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow.findRouteNavigator()
        val viewModel = getScreenModel<SearchViewModel>()
        val state by viewModel.state.collectAsState()

        SearchIdleContent(
            lastSearchItems = state.lastSearchItems,
            recommendedSearchItems = state.recommendedList,
            interestsSearchItems = state.interestsList,
            onSearchItemClick = { searchUiModel ->
                navigator.push(WebViewRoute(searchUiModel.title, searchUiModel.articleUrl))
            },
            onSearchClick = {
                navigator.push(ActiveSearchScreenRoute())
            },
        )
    }

    @Composable
    private fun SearchIdleContent(
        modifier: Modifier = Modifier,
        lastSearchItems: List<SearchUiModel> = emptyList(),
        recommendedSearchItems: List<SearchUiModel> = emptyList(),
        interestsSearchItems: List<SearchUiModel> = emptyList(),
        onSearchItemClick: (SearchUiModel) -> Unit = {},
        onSearchClick: () -> Unit = {},
    ) {
        Column(modifier.verticalScroll(rememberScrollState())) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable { onSearchClick() }
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Row(
                    modifier = Modifier.height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(16.dp))

                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = stringResource(MR.strings.search),
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = stringResource(MR.strings.search_placeholder)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (lastSearchItems.isNotEmpty()) {
                HorizontalSearchItemsListElement(
                    modifier = Modifier,
                    title = stringResource(MR.strings.last_search),
                    searchUiItems = lastSearchItems,
                    onSearchItemClick = onSearchItemClick,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalSearchItemsListElement(
                modifier = Modifier,
                title = stringResource(MR.strings.recommended_for_you),
                searchUiItems = recommendedSearchItems,
                onSearchItemClick = onSearchItemClick,
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalSearchItemsListElement(
                modifier = Modifier,
                title = stringResource(MR.strings.interests),
                searchUiItems = interestsSearchItems,
                onSearchItemClick = onSearchItemClick,
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}