package com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache

import kotlinx.coroutines.flow.Flow

interface BookmarksDao {
    suspend fun getBookmarks(): List<BookmarkEntity>
    fun getBookmarksStream(): Flow<List<BookmarkEntity>>
    suspend fun getBookmarkById(id: String): BookmarkEntity
    suspend fun insertOrReplace(bookmarks: List<BookmarkEntity>)
    suspend fun deleteBookmarkById(id: String)
}