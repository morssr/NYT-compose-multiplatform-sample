package com.mls.kmp.mor.nytnewskmp.ui

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.ArticlesRepository
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.BookmarksRepository
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarkModel
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.data.common.defaultTopics
import com.mls.kmp.mor.nytnewskmp.ui.articles.ArticleUIModel
import com.mls.kmp.mor.nytnewskmp.ui.articles.toArticleUI
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "HomeScreenViewModel"

class HomeScreenViewModel(
    private val repository: ArticlesRepository,
    private val bookmarksRepository: BookmarksRepository,
    logger: Logger
) : StateScreenModel<HomeScreenState>(HomeScreenState()) {

    private val log = logger.withTag(TAG)

    init {
        launchTopicsUpdatesHotFlow()
        launchArticlesAndBookmarksUpdatesHotFlow()
    }

    private fun launchTopicsUpdatesHotFlow() {
        log.d { "launchTopicsUpdatesFlow() called" }
        repository.getMyTopicsListStream()
            .onEach { topics -> mutableState.update { it.copy(topics = topics) } }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = defaultTopics
            ).launchIn(coroutineScope)
    }

    private fun launchArticlesAndBookmarksUpdatesHotFlow() {
        log.d { "launchArticlesAndBookmarksUpdatesFlow() called" }
        mutableState
            .map { it.currentTopic }
            .distinctUntilChanged()
            .debounce(350)
            .flatMapLatest { topic ->
                repository.getArticlesStreamIfRequired(topic = topic)
            }
            .combine(bookmarksRepository.getBookmarksStream()) { response, bookmarks ->

                when (response) {
                    is Response.Loading -> ArticlesFeedState.Loading
                    is Response.Failure -> ArticlesFeedState.Error(
                        response.error,
                        response.fallbackData?.map { article ->
                            article.toArticleUI(bookmarked = bookmarks.any { it.id == it.id })
                        } ?: emptyList()
                    )

                    is Response.Success -> {
                        ArticlesFeedState.Success(
                            response.data.map { article ->
                                article.toArticleUI(bookmarked = bookmarks.any { it.id == article.id })
                            }
                        )
                    }
                }
            }
            .onEach { state ->
                mutableState.update {
                    it.copy(
                        feedsStates = it.feedsStates.toMutableMap().apply {
                            this[it.currentTopic] = state
                        }
                    )
                }
            }
            .stateIn(
                coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = Response.Loading
            )
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

    fun updateBookmarks(articleId: String, bookmarked: Boolean) {
        log.d { "onBookmarkClick() called with: article = $articleId" }
        coroutineScope.launch {
            val articleModel = repository.getArticleById(articleId)
            if (bookmarked) {
                bookmarksRepository.deleteBookmarkById(articleModel.id)
            } else {
                bookmarksRepository.saveBookmarks(listOf(articleModel.toBookmarkModel(state.value.currentTopic)))
            }
        }
    }
}

data class HomeScreenState(
    val currentTopic: Topics = Topics.HOME,
    val topics: List<Topics> = listOf(Topics.HOME),
    val feedsStates: Map<Topics, ArticlesFeedState> = mapOf(currentTopic to ArticlesFeedState.Loading),
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

