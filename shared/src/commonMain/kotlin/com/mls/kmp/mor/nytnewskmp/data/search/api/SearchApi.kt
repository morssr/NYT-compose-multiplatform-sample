package com.mls.kmp.mor.nytnewskmp.data.search.api

import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse

interface SearchApi {
    suspend fun getSearchResults(query: String, page: Int = 0): ApiResponse<SearchResponse>
}