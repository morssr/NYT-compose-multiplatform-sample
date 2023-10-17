package com.mls.kmp.mor.nytnewskmp.data.popular.api

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.core.api.API_KEY
import com.mls.kmp.mor.nytnewskmp.core.api.ApiClient
import com.mls.kmp.mor.nytnewskmp.core.api.POPULAR_REQUEST_BASE_URL
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularPeriod
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val TAG = "PopularApiImpl"

class PopularApiImpl(
    private val apiClient: ApiClient,
    logger: Logger
) : PopularApi {

    private val log = logger.withTag(TAG)

    override suspend fun getPopularArticles(
        category: PopularCategory,
        period: PopularPeriod
    ): ApiResponse<PopularsResponse> =
        try {
            log.v { "getPopularArticles() called" }
            val response: PopularsResponse =
                apiClient.client.get(
                    buildUrl(
                        baseUrl = POPULAR_REQUEST_BASE_URL,
                        category = category,
                        period = period
                    )
                ).body()
            log.v { "getPopularArticles() response: $response" }
            ApiResponse.Success(response)
        } catch (e: Exception) {
            log.e { "getPopularArticles() error: ${e.message}" }
            ApiResponse.Failure(e)
        }

    private fun buildUrl(
        baseUrl: String = POPULAR_REQUEST_BASE_URL,
        category: PopularCategory = PopularCategory.MOST_VIEWED,
        period: PopularPeriod = PopularPeriod.DAY
    ): String {
        val url = "${baseUrl}/${category}/${period}.json?api-key=${API_KEY}"
        log.v { "buildUrl() url: $url" }
        return url
    }

}