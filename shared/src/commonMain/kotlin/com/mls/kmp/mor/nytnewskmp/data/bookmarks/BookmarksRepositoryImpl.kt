package com.mls.kmp.mor.nytnewskmp.data.bookmarks

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarksDao
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.BookmarkModel
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarkEntity
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarkModel
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarksModelList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "BookmarksRepositoryImpl"

class BookmarksRepositoryImpl(
    private val bookmarksDao: BookmarksDao,
    logger: Logger
) : BookmarksRepository {

    private val log = logger.withTag(TAG)

    override suspend fun getBookmarks(): List<BookmarkModel> {
        log.v { "getBookmarks() called" }
        return bookmarksDao.getBookmarks().map { it.toBookmarkModel() }
    }

    override fun getBookmarksStream(): Flow<List<BookmarkModel>> {
        log.v { "getBookmarksStream() called" }
        return bookmarksDao.getBookmarksStream().map { it.toBookmarksModelList() }
    }

    override suspend fun getBookmarkById(id: String): BookmarkModel {
        log.v { "getBookmarkById() called with id: $id" }
        return bookmarksDao.getBookmarkById(id).toBookmarkModel()
    }

    override suspend fun saveBookmarks(bookmarks: List<BookmarkModel>) {
        log.v { "saveBookmarks() called with Bookmarks list size: ${bookmarks.size}" }
        bookmarksDao.insertOrReplace(bookmarks.map { it.toBookmarkEntity() })
    }

    override suspend fun deleteBookmarkById(id: String) {
        log.v { "deleteBookmarkById() called with id: $id" }
        bookmarksDao.deleteBookmarkById(id)
    }
}