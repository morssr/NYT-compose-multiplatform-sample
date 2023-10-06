package com.mls.kmp.mor.nytnewskmp.ui.search

import androidx.paging.LoadStates
import app.cash.paging.PagingData
import app.cash.paging.map
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SearchScreenViewModel"

class SearchViewModel(
    private val repository: SearchRepository,
    logger: Logger
) : StateScreenModel<SearchScreenState>(SearchScreenState()) {

    val log = logger.withTag(TAG)

    init {
        log.v { "init called" }
        repository.getLastSearch()
            .map { it.toSearchUiModels() }
            .onEach { lastSearchItems -> mutableState.update { it.copy(lastSearchItems = lastSearchItems) } }
            .stateIn(
                coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).launchIn(coroutineScope)

        repository.getInterestsList()
            .map { it.toSearchUiModels() }
            .onEach { interestsList -> mutableState.update { it.copy(interestsList = interestsList) } }
            .stateIn(
                coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).launchIn(coroutineScope)

        repository.getRecommendedList()
            .map { it.toSearchUiModels() }
            .onEach { recommendedList -> mutableState.update { it.copy(recommendedList = recommendedList) } }
            .stateIn(
                coroutineScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            ).launchIn(coroutineScope)
    }

    fun searchStories(query: String) {
        log.d { "searchStories() called with query: $query" }
        mutableState.update { state ->
            state.copy(
                searchQuery = query,
                searchResults = repository.searchStoriesPagingSource(query)
                    .flow
                    .map { pagingData -> pagingData.map { it.toSearchUiModel() } })
        }
    }

    fun bookmarkArticle(searchUiModel: SearchUiModel) {
        log.d { "bookmarkArticle() called with searchUiModel: $searchUiModel" }
        coroutineScope.launch {
            repository.bookmarkSearchItem(searchUiModel.toSearchModel())
        }
    }
}

data class SearchScreenState(
    val searchQuery: String = "",
    val searchResults: Flow<PagingData<SearchUiModel>> = flow {
        emit(
            PagingData.empty(
                sourceLoadStates = LoadStates(
                    refresh = androidx.paging.LoadState.NotLoading(false),
                    prepend = androidx.paging.LoadState.NotLoading(false),
                    append = androidx.paging.LoadState.NotLoading(false),
                )
            )
        )
    },
    val lastSearchItems: List<SearchUiModel> = emptyList(),
    val interestsList: List<SearchUiModel> = emptyList(),
    val recommendedList: List<SearchUiModel> = emptyList()
)