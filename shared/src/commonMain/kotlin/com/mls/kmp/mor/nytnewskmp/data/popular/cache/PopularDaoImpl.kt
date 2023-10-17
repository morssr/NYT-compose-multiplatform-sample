package com.mls.kmp.mor.nytnewskmp.data.popular.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import com.mls.kmp.mor.nytnewskmp.data.popular.common.toPopularDbObject
import com.mls.kmp.mor.nytnewskmp.data.popular.common.toPopularEntityList
import com.mls.kmp.mor.nytnewskmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

private const val TAG = "PopularDaoImpl"

class PopularDaoImpl(
    dbInstance: AppDatabase,
    private val coroutineContext: CoroutineContext,
    logger: Logger
) : PopularDao {
    private val log = logger.withTag(TAG)
    private val popularDb = dbInstance.popularsQueries

    override fun getPopularsStream(category: PopularCategory): Flow<List<PopularEntity>> {
        log.v { "getPopularsStream() called" }
        return popularDb.getAllPopulars().asFlow().mapToList(coroutineContext)
            .map { it.toPopularEntityList() }
    }

    override suspend fun insertOrReplace(populars: List<PopularEntity>) {
        log.v { "insertOrReplace() called with Populars list size: ${populars.size}" }
        popularDb.transaction {
            populars.forEach { popular ->
                popularDb.insertOrReplacePopulars(popular.toPopularDbObject())
            }
        }
    }

    override suspend fun deleteAllPopulars() {
        log.v { "deleteAllPopulars() called" }
        popularDb.deleteAllPopulars()
    }
}