package com.mls.kmp.mor.nytnewskmp.data.search.cache

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResult
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.search.SearchModel
import com.mls.kmp.mor.nytnewskmp.data.search.api.SearchApi
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchEntities
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchEntity
import com.mls.kmp.mor.nytnewskmp.data.search.toSearchModel
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse

private const val TAG = "SearchRemoteMediator"

class SearchPagingSource(
    private val query: String,
    private val searchDao: SearchDao,
    private val searchApi: SearchApi,
    logger: Logger
) : PagingSource<Int, SearchModel>() {

    private val log = logger.withTag(TAG)

    override suspend fun load(params: PagingSourceLoadParams<Int>): PagingSourceLoadResult<Int, SearchModel> {
        log.d { "load: params: ${params.key}" }
        val page = params.key ?: FIRST_PAGE_INDEX
        when (val response = searchApi.getSearchResults(query = query, page = page)) {
            is ApiResponse.Failure -> {
                log.e { "load: error: ${response.error}" }
                return PagingSourceLoadResultError(
                    Exception(response.error)
                )
            }

            is ApiResponse.Success -> {
                log.d { "load: page: $page | status: ${response.data.status} | list size: ${response.data.response.docs.size}" }
                if (page == FIRST_PAGE_INDEX) {
                    searchDao.deleteAllSearchResults()
                    searchDao.insertOrReplace(response.data.response.docs.toSearchEntities())
                }
                return PagingSourceLoadResultPage(
                    data = response.data.response.docs
                        .map { it.toSearchEntity() }
                        .map { it.toSearchModel() },
                    prevKey = if (page == 0) null else page - 1,
                    nextKey = if (response.data.response.docs.isEmpty()) null else page + 1
                )
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchModel>): Int? {
        log.d { "getRefreshKey: ${state.anchorPosition}" }
        return null
    }

    companion object {
        const val FIRST_PAGE_INDEX = 0
    }
}