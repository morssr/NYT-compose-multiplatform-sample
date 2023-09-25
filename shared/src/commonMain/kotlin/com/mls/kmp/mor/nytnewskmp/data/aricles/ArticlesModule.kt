package com.mls.kmp.mor.nytnewskmp.data.aricles

import com.mls.kmp.mor.nytnewskmp.data.aricles.api.ArticlesApi
import com.mls.kmp.mor.nytnewskmp.data.aricles.api.ArticlesApiImpl
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDao
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticlesDaoImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
    single<ArticlesRepository> {
        ArticlesRepositoryImpl(
            articlesApi = get(),
            articlesDao = get(),
            logger = get(),
        )
    }
}