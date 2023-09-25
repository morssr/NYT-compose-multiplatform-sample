package com.mls.kmp.mor.nytnewskmp.ui

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.ArticlesRepository
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticleUIModel
import com.mls.kmp.mor.nytnewskmp.ui.articles.toArticleUIList
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

private const val TAG = "HomeScreenViewModel"

class HomeScreenViewModel(
    private val repository: ArticlesRepository,
    logger: Logger
) : StateScreenModel<HomeScreenState>(HomeScreenState.Loading) {

    private val log = logger.withTag(TAG)

    init {
        repository.getStoriesByTopicStream(topic = Topics.HOME, remoteSync = true)
            .stateIn(
                coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = Response.Loading
            )
            .onEach { response ->
                val state = when (response) {
                    is Response.Loading -> HomeScreenState.Loading
                    is Response.Success -> HomeScreenState.Success(response.data.toArticleUIList())
                    is Response.Failure -> HomeScreenState.Error
                }
                mutableState.update { state }
            }.launchIn(coroutineScope)
    }
}

sealed class HomeScreenState {
    object Loading : HomeScreenState()
    object Error : HomeScreenState()
    data class Success(val data: List<ArticleUIModel>) : HomeScreenState()
}

