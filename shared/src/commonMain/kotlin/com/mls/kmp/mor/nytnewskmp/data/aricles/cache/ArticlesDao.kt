package com.mls.kmp.mor.nytnewskmp.data.aricles.cache

import kotlinx.coroutines.flow.Flow

interface ArticlesDao {

    fun getArticleById(id: String): ArticleEntity
    fun getAllArticles(): List<ArticleEntity>
    fun getAllArticlesStream(): Flow<List<ArticleEntity>>
    fun getArticlesByTopic(topic: String): List<ArticleEntity>
    fun getArticlesStreamByTopic(topic: String): Flow<List<ArticleEntity>>
    fun insertOrReplace(articles: List<ArticleEntity>)
    suspend fun deleteAllByTopic(topic: String)
    suspend fun deleteAllArticles()
}