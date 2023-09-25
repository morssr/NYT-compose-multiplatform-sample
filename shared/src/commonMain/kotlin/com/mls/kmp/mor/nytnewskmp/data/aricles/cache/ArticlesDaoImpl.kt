package com.mls.kmp.mor.nytnewskmp.data.aricles.cache

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleDbObject
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleEntity
import com.mls.kmp.mor.nytnewskmp.data.aricles.common.toArticleEntityList
import com.mls.kmp.mor.nytnewskmp.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

private const val TAG = "ArticlesDaoImpl"

class ArticlesDaoImpl(
    dbInstance: AppDatabase,
    private val coroutineContext: CoroutineContext,
    logger: Logger
) : ArticlesDao {

    private val log = logger.withTag(TAG)
    private val articlesDb = dbInstance.articlesQueries

    override fun getArticleById(id: String): ArticleEntity {
        log.v { "getArticleById() called with id: $id" }
        return articlesDb.getArticleById(id).executeAsOne().toArticleEntity()
    }

    override fun getAllArticles(): List<ArticleEntity> {
        log.v { "getAllArticles() called" }
        return articlesDb.getAllArticles().executeAsList().map { it.toArticleEntity() }
    }

    override fun getAllArticlesStream(): Flow<List<ArticleEntity>> {
        log.v { "getAllArticlesStream() called" }
        return articlesDb.getAllArticles().asFlow().mapToList(coroutineContext)
            .map { it.toArticleEntityList() }
    }

    override fun getArticlesByTopic(topic: String): List<ArticleEntity> {
        log.v { "getArticlesByTopic() called with topic: $topic" }
        return articlesDb.getArticlesByTopic(topic).executeAsList().map { it.toArticleEntity() }
    }

    override fun getArticlesStreamByTopic(topic: String): Flow<List<ArticleEntity>> {
        log.v { "getArticlesBySectionTopic() called with topic: $topic" }
        return articlesDb.getArticlesByTopic(topic).asFlow().mapToList(coroutineContext)
            .map { it.toArticleEntityList() }
    }

    override fun insertOrReplace(articles: List<ArticleEntity>) {
        log.v { "insertOrReplace() called with Articles list size: ${articles.size}" }
        articlesDb.transaction {
            articles.forEach { article ->
                articlesDb.insertOrReplaceArticles(article.toArticleDbObject())
            }
        }
    }

    override suspend fun deleteAllByTopic(topic: String) {
        log.v { "deleteAllByTopic() called with topic: $topic" }
        articlesDb.deleteArticlesByTopic(topic)
    }

    override suspend fun deleteAllArticles() {
        log.v { "deleteAllArticles() called" }
        articlesDb.deleteAllArticles()
    }
}