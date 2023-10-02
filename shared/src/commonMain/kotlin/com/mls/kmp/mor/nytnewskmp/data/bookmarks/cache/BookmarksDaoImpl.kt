package com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarkDbObject
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarkEntity
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.toBookmarksEntitiesList
import com.mls.kmp.mor.nytnewskmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

private const val TAG = "BookmarksDaoImpl"

class BookmarksDaoImpl(
    dbInstance: AppDatabase,
    private val coroutineContext: CoroutineContext,
    logger: Logger
) : BookmarksDao {

    private val log = logger.withTag(TAG)
    private val bookmarksDb = dbInstance.bookmarksQueries

    override suspend fun getBookmarks(): List<BookmarkEntity> {
        log.v { "getBookmarks() called" }
        return bookmarksDb.getAllBookmarks().executeAsList().map { it.toBookmarkEntity() }
    }

    override fun getBookmarksStream(): Flow<List<BookmarkEntity>> {
        log.v { "getBookmarksStream() called" }
        return bookmarksDb.getBookmarksSteamDesc().asFlow().mapToList(coroutineContext)
            .map { it.toBookmarksEntitiesList() }
    }

    override suspend fun getBookmarkById(id: String): BookmarkEntity {
        log.v { "getBookmarkById() called with id: $id" }
        return bookmarksDb.getbookmarkById(id).executeAsOne().toBookmarkEntity()
    }

    override suspend fun insertOrReplace(bookmarks: List<BookmarkEntity>) {
        log.v { "insertOrReplace() called with Bookmarks list size: ${bookmarks.size}" }
        bookmarksDb.transaction {
            bookmarks.forEach { bookmark ->
                bookmarksDb.insertOrReplaceBookmark(bookmark.toBookmarkDbObject())
            }
        }
    }

    override suspend fun deleteBookmarkById(id: String) {
        log.v { "deleteBookmarkById() called with id: $id" }
        bookmarksDb.deleteBookmarkById(id)
    }
}