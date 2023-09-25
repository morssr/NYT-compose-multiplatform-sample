package com.mls.kmp.mor.nytnewskmp.data.aricles

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.api.ArticlesApi
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDao
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleEntityList
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleModel
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleModelList
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG = "ArticlesRepositoryImpl"

class ArticlesRepositoryImpl(
    private val articlesApi: ArticlesApi,
    private val articlesDao: ArticlesDao,
    logger: Logger,
) : ArticlesRepository {

    private val log = logger.withTag(TAG)

    override suspend fun getArticleById(id: String): ArticleModel {
        log.v { "getArticleById() called with id: $id" }
        return articlesDao.getArticleById(id).toArticleModel()
    }

    override fun getStoriesByTopicStream(
        topic: Topics,
        remoteSync: Boolean
    ): Flow<Response<List<ArticleModel>>> = flow {
        log.v { "getArticlesByTopic() called with topic: $topic | remote sync: $remoteSync" }
        emit(Response.Loading)

        if (!remoteSync) {
            emitAll(
                articlesDao.getAllArticlesStream().map {
                    Response.Success(it.toArticleModelList())
                }
            )
        } else {
            when (val result = refreshArticlesByTopic(topic)) {
                is ApiResponse.Success -> {
                    log.v { "getArticlesByTopic() success" }
                    emitAll(
                        articlesDao.getArticlesStreamByTopic(topic.topicName).map {
                            Response.Success(it.toArticleModelList())
                        }
                    )
                }

                is ApiResponse.Failure -> {
                    log.e { "getArticlesByTopic() failed to fetch articles from API. error: ${result.error}" }
                    emitAll(
                        articlesDao.getArticlesStreamByTopic(topic.topicName).map {
                            Response.Failure(result.error, it.toArticleModelList())
                        }
                    )
                }
            }
        }
    }

    override suspend fun refreshArticlesByTopic(topic: Topics): ApiResponse<Unit> {
        log.v { "refreshArticlesByTopic() called with topic: $topic" }
        return when (val articles = articlesApi.getTopics(topic)) {
            is ApiResponse.Success -> {
                log.v { "refreshArticlesByTopic() success" }
                articlesDao.deleteAllByTopic(topic.topicName)
                articlesDao.insertOrReplace(articles.data.results.toArticleEntityList(topic))
                ApiResponse.Success(Unit)
            }

            is ApiResponse.Failure -> {
                log.e { "refreshArticlesByTopic() failed to fetch articles from API. error: ${articles.error}" }
                ApiResponse.Failure(articles.error)
            }
        }
    }

    override fun getMyTopicsListStream(): Flow<List<Topics>> {
        TODO()
    }

    override suspend fun updateMyTopicsList(topicsList: List<Topics>) {
        TODO()
    }
}