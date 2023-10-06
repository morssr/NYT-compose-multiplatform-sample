package com.mls.kmp.mor.nytnewskmp.data.search.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.search.fromSearchDbToSearchEntities
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchDbObject
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchEntity
import com.mls.kmp.mor.nytnewskmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

private const val TAG = "SearchDaoImpl"

class SearchDaoImpl(
    dbInstance: AppDatabase,
    private val coroutineContext: CoroutineContext,
    logger: Logger
) : SearchDao {

    private val log = logger.withTag(TAG)
    private val searchDb = dbInstance.searchQueries

    override suspend fun getSearchById(id: String): SearchEntity {
        log.v { "getSearchById called with id: $id" }
        return searchDb.getSearchById(id).executeAsOne().toSearchEntity()
    }

    override fun getSearchResultsStream(): Flow<List<SearchEntity>> {
        log.v { "getSearchResultsStream called()" }
        return searchDb.getAllSearches().asFlow().mapToList(coroutineContext)
            .map { it.fromSearchDbToSearchEntities() }
    }

    override fun insertOrReplace(items: List<SearchEntity>) {
        log.v { "insertOrReplace called with list items size: ${items.size}" }
        searchDb.transaction {
            items.forEach {
                searchDb.insertOrReplaceSearch(it.toSearchDbObject())
            }
        }
    }

    override suspend fun deleteAllSearchResults() {
        log.v { "deleteAllSearchResults called" }
        searchDb.clearAllSearches()
    }
}