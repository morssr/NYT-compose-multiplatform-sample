package com.mls.kmp.mor.nytnewskmp.data.search.api

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.core.api.API_KEY
import com.mls.kmp.mor.nytnewskmp.core.api.ApiClient
import com.mls.kmp.mor.nytnewskmp.core.api.SEARCH_REQUEST_BASE_URL
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val TAG = "SearchApiImpl"

class SearchApiImpl(
    private val apiClient: ApiClient,
    logger: Logger
) : SearchApi {

    private val log = logger.withTag(TAG)

    override suspend fun getSearchResults(query: String, page: Int): ApiResponse<SearchResponse> =
        try {
            log.v { "getSearchResults() called with query: $query | page: $page" }
            val response: SearchResponse =
                apiClient.client.get(buildUrl(query = query, page = page)).body()
            log.v { "getSearchResults() response: ${response.status}" }
            ApiResponse.Success(response)
        } catch (e: Exception) {
            log.e { "getSearchResults() error: ${e.message}" }
            ApiResponse.Failure(e)
        }

    private fun buildUrl(query: String, page: Int): String {
        log.v { "buildUrl() called with query: $query | page: $page" }
        val url = "${ SEARCH_REQUEST_BASE_URL }q=$query&page=$page&api-key=$API_KEY"
        log.v { "buildUrl() url: $url" }
        return url
    }
}