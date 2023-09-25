package com.mls.kmp.mor.nytnewskmp.data.aricles.api

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.core.api.API_KEY
import com.mls.kmp.mor.nytnewskmp.core.api.ApiClient
import com.mls.kmp.mor.nytnewskmp.core.api.TOPICS_REQUEST_BASE_URL
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val TAG = "ArticlesApiImpl"

class ArticlesApiImpl(
    private val apiClient: ApiClient,
    logger: Logger
) : ArticlesApi {

    private val log = logger.withTag(TAG)

    override suspend fun getTopics(topic: Topics): ApiResponse<TopicsResponse> =
        try {
            log.v { "getTopics() called with topic: $topic" }
            val response: TopicsResponse =
                apiClient.client.get(buildUrl(topic = topic)).body()
            log.v { "getTopics() response: $response" }
            ApiResponse.Success(response)
        } catch (e: Exception) {
            log.e { "getTopics() error: ${e.message}" }
            ApiResponse.Failure(e)
        }

    private fun buildUrl(
        baseUrl: String = TOPICS_REQUEST_BASE_URL,
        topic: Topics,
        apiKey: String = API_KEY
    ): String {
        log.v { "buildUrl() called with topic: $topic | BaseUrl: $baseUrl | apiKey: $apiKey" }
        val url = "$TOPICS_REQUEST_BASE_URL${topic.topicName}.json?api-key=$API_KEY"
        log.v { "buildUrl() url: $url" }
        return url
    }

}