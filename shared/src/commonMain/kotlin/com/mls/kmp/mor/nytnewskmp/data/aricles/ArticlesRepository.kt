package com.mls.kmp.mor.nytnewskmp.data.aricles

import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.Flow

interface ArticlesRepository {
    suspend fun getArticleById(id: String): ArticleModel

    fun getStoriesByTopicStream(
        topic: Topics = Topics.HOME,
        remoteSync: Boolean = true
    ): Flow<Response<List<ArticleModel>>>

    suspend fun refreshArticlesByTopic(topic: Topics): ApiResponse<Unit>
    fun getMyTopicsListStream(): Flow<List<Topics>>
    suspend fun updateMyTopicsList(topicsList: List<Topics>)
}