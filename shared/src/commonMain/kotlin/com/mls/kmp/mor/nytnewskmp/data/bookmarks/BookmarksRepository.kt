package com.mls.kmp.mor.nytnewskmp.data.bookmarks

import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.BookmarkModel
import kotlinx.coroutines.flow.Flow

interface BookmarksRepository {
    suspend fun getBookmarks(): List<BookmarkModel>
    fun getBookmarksStream(): Flow<List<BookmarkModel>>
    suspend fun getBookmarkById(id: String): BookmarkModel
    suspend fun saveBookmarks(bookmarks: List<BookmarkModel>)
    suspend fun deleteBookmarkById(id: String)
}