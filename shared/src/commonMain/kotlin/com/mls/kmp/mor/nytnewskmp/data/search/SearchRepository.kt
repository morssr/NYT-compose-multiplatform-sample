package com.mls.kmp.mor.nytnewskmp.data.search

import app.cash.paging.Pager
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchStoriesPagingSource(query: String): Pager<Int, SearchModel>
    fun getLastSearch(): Flow<List<SearchModel>>
    fun getInterestsList(): Flow<List<SearchModel>>
    fun getRecommendedList(): Flow<List<SearchModel>>
    suspend fun getSearchItemById(id: String): SearchModel
    suspend fun bookmarkSearchItem(searchModel: SearchModel)
}