package com.mls.kmp.mor.nytnewskmp.data.search.cache

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDao
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleModelList
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarksDao
import com.mls.kmp.mor.nytnewskmp.data.search.SearchModel
import com.mls.kmp.mor.nytnewskmp.data.search.SearchRepository
import com.mls.kmp.mor.nytnewskmp.data.search.api.SearchApi
import com.mls.kmp.mor.nytnewskmp.data.search.fromArticleToSearchModels
import com.mls.kmp.mor.nytnewskmp.data.search.toBookmarkEntity
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchModel
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchModels
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val TAG = "SearchRepositoryImpl"

class SearchRepositoryImpl(
    private val searchDao: SearchDao,
    private val searchApi: SearchApi,
    private val articlesDao: ArticlesDao,
    private val bookmarksDao: BookmarksDao,
    private val logger: Logger
) : SearchRepository {

    val log = logger.withTag(TAG)

    override fun searchStoriesPagingSource(query: String): Pager<Int, SearchModel> {
        log.d { "searchStoriesPagingSource called with query: $query" }
        val pagingConfig = PagingConfig(pageSize = 10, initialLoadSize = 20)
        return Pager(pagingConfig) {
            SearchPagingSource(
                query = query,
                searchDao = searchDao,
                searchApi = searchApi,
                logger = logger
            )
        }
    }

    override fun getLastSearch(): Flow<List<SearchModel>> {
        log.d { "getLastSearch called" }
        return searchDao.getSearchResultsStream()
            .map { it.toSearchModels() }
    }

    override fun getInterestsList(): Flow<List<SearchModel>> =
        articlesDao
            .getAllArticlesStream()
            .map { it.distinctBy { articleEntity -> articleEntity.topic } }
            .map { it.toArticleModelList() }
            .map { it.fromArticleToSearchModels() }


    override fun getRecommendedList(): Flow<List<SearchModel>> = articlesDao
        .getAllArticlesStream()
        .map { it.shuffled().take(10) }
        .map { it.toArticleModelList() }
        .map { it.fromArticleToSearchModels() }

    override suspend fun getSearchItemById(id: String): SearchModel {
        log.d { "getStoryById called with id: $id" }
        return searchDao.getSearchById(id).toSearchModel()
    }

    override suspend fun bookmarkSearchItem(searchModel: SearchModel) {
        log.d { "bookmarkSearchItem called with searchModel: $searchModel" }
        bookmarksDao.insertOrReplace(listOf(searchModel.toBookmarkEntity()))
    }
}