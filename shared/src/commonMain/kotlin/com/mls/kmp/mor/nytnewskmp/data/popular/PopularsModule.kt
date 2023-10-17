package com.mls.kmp.mor.nytnewskmp.data.popular

import com.mls.kmp.mor.nytnewskmp.data.popular.api.PopularApi
import com.mls.kmp.mor.nytnewskmp.data.popular.api.PopularApiImpl
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularDao
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularDaoImpl
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module

val popularsModule = module {

    single<PopularApi> {
        PopularApiImpl(
            apiClient = get(),
            logger = get(),
        )
    }

    single<PopularDao> {
        PopularDaoImpl(
            dbInstance = get(),
            //TODO inject dispatcher
            coroutineContext = Dispatchers.IO,
            logger = get(),
        )
    }

    single<PopularRepository> {
        PopularRepositoryImpl(
            api = get(),
            dao = get(),
            logger = get(),
        )
    }
}