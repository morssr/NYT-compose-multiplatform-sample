package com.mls.kmp.mor.nytnewskmp.ui.bookmarks

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.BookmarksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "BookmarksViewModel"

class BookmarksScreenViewModel(
    private val repository: BookmarksRepository,
    logger: Logger
) : StateScreenModel<BookmarksScreenState>(BookmarksScreenState()) {

    private val log = logger.withTag(TAG)

    init {
        repository.getBookmarksStream()
            .map { bookmarks ->
                bookmarks.map { it.toBookmarkUiModel() }
            }
            .onEach { bookmarks -> mutableState.update { it.copy(bookmarks = bookmarks) } }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            ).launchIn(coroutineScope)
    }

    fun deleteBookmark(id: String) {
        log.d { "deleteBookmarkById() called with id: $id" }
        coroutineScope.launch {
            repository.deleteBookmarkById(id)
        }
    }
}

data class BookmarksScreenState(
    val bookmarks: List<BookmarkUiModel> = emptyList()
)