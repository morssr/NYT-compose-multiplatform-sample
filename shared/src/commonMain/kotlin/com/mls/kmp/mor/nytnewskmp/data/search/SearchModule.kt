package com.mls.kmp.mor.nytnewskmp.data.search

import com.mls.kmp.mor.nytnewskmp.data.search.api.SearchApi
import com.mls.kmp.mor.nytnewskmp.data.search.api.SearchApiImpl
import com.mls.kmp.mor.nytnewskmp.data.search.cache.SearchDao
import com.mls.kmp.mor.nytnewskmp.data.search.cache.SearchDaoImpl
import com.mls.kmp.mor.nytnewskmp.data.search.cache.SearchRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val searchModule = module {
    single<SearchApi> { SearchApiImpl(apiClient = get(), logger = get()) }

    single<SearchDao> {
        SearchDaoImpl(
            dbInstance = get(),
            coroutineContext = Dispatchers.IO,
            logger = get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryImpl(
            searchApi = get(),
            searchDao = get(),
            articlesDao = get(),
            bookmarksDao = get(),
            logger = get()
        )
    }

}