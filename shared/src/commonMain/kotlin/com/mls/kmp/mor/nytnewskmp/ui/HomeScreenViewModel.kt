package com.mls.kmp.mor.nytnewskmp.ui

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.ArticlesRepository
import com.mls.kmp.mor.nytnewskmp.data.aricles.defaultTopics
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticleUIModel
import com.mls.kmp.mor.nytnewskmp.ui.articles.toArticleUIList
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "HomeScreenViewModel"

class HomeScreenViewModel(
    private val repository: ArticlesRepository,
    logger: Logger
) : StateScreenModel<HomeScreenState>(HomeScreenState()) {

    private val log = logger.withTag(TAG)

    init {
        log.v { "init()" }
        repository.getMyTopicsListStream()
            .onEach { topics -> mutableState.update { it.copy(topics = topics) } }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = defaultTopics
            ).launchIn(coroutineScope)


        val topic = Topics.HOME
        repository.getStoriesByTopicStream(topic = Topics.HOME)
            .stateIn(
                coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = Response.Loading
            )
            .map { response ->
                when (response) {
                    is Response.Loading -> ArticlesFeedState.Loading
                    is Response.Failure -> ArticlesFeedState.Error(
                        response.error,
                        response.fallbackData?.toArticleUIList() ?: emptyList()
                    )

                    is Response.Success -> ArticlesFeedState.Success(response.data.toArticleUIList())
                }
            }
            .onEach { state ->
                mutableState.update {
                    it.copy(
                        feedsStates = it.feedsStates.toMutableMap().apply {
                            this[topic] = state
                        }
                    )
                }
            }
            .launchIn(coroutineScope)
    }

    fun refreshCurrentTopic(topic: Topics) {
        log.d { "refreshCurrentTopic() called with: topic = $topic" }
        mutableState.update { it.copy(currentTopic = topic) }
    }

    fun updateTopics(topics: List<Topics>) {
        log.d { "updateTopic() called with: topics = $topics" }
        coroutineScope.launch() {
            repository.updateMyTopicsList(topics)
        }
    }

}

data class HomeScreenState(
    val currentTopic: Topics = Topics.HOME,
    val topics: List<Topics> = listOf(Topics.HOME),
    val feedsStates: Map<Topics, ArticlesFeedState> = mapOf(Topics.HOME to ArticlesFeedState.Loading),
    val dialogSelector: DialogSelector = DialogSelector.None
)

sealed class ArticlesFeedState {
    object Loading : ArticlesFeedState()
    data class Error(val error: Exception, val fallbackData: List<ArticleUIModel>) : ArticlesFeedState()
    data class Success(val data: List<ArticleUIModel>) : ArticlesFeedState()
}

sealed class DialogSelector {
    object MainMenuDropdown : DialogSelector()
    object Settings : DialogSelector()
    object AboutUs : DialogSelector()
    object ContactUs : DialogSelector()
    object EmailChooser : DialogSelector()
    object None : DialogSelector()
}

