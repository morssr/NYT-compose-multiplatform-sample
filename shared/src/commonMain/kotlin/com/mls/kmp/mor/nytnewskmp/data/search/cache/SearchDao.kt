package com.mls.kmp.mor.nytnewskmp.data.search.cache

import kotlinx.coroutines.flow.Flow

interface SearchDao {
    suspend fun getSearchById(id: String): SearchEntity
    fun getSearchResultsStream(): Flow<List<SearchEntity>>
    fun insertOrReplace(items: List<SearchEntity>)
    suspend fun deleteAllSearchResults()
}