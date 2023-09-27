package com.mls.kmp.mor.nytnewskmp.data.aricles

import com.mls.kmp.mor.nytnewskmp.data.aricles.api.ArticlesApi
import com.mls.kmp.mor.nytnewskmp.data.aricles.api.ArticlesApiImpl
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDao
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDaoImpl
import com.mls.kmp.mor.nytnewskmp.data.common.SyncManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.dsl.module

val articlesModule = module {
    single<ArticlesApi> { ArticlesApiImpl(apiClient = get(), logger = get()) }

    single<ArticlesDao> {
        ArticlesDaoImpl(
            dbInstance = get(),
            //TODO inject dispatcher
            coroutineContext = Dispatchers.IO,
            logger = get(),
        )
    }

    single<SyncManager> {
        ArticlesSyncManager(
            preferences = get(qualifier = named(TOPICS_PREFERENCES_FILE_NAME)),
            logger = get()
        )
    }

    single<ArticlesRepository> {
        ArticlesRepositoryImpl(
            articlesApi = get(),
            articlesDao = get(),
            articlesSyncManager = get(),
            preferences = get(qualifier = named(TOPICS_PREFERENCES_FILE_NAME)),
            logger = get(),
        )
    }
}