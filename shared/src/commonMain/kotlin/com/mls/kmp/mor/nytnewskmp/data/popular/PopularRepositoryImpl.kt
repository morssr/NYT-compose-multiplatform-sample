package com.mls.kmp.mor.nytnewskmp.data.popular

import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.popular.api.PopularApi
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularDao
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularModel
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularPeriod
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularRepository
import com.mls.kmp.mor.nytnewskmp.data.popular.common.toPopularEntityList
import com.mls.kmp.mor.nytnewskmp.data.popular.common.toPopularModelList
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

private const val TAG = "PopularRepositoryImpl"

class PopularRepositoryImpl(
    private val api: PopularApi,
    private val dao: PopularDao,
    logger: Logger,
) : PopularRepository {

    private val log = logger.withTag(TAG)

    override fun getPopularsByTypeStream(
        category: PopularCategory,
        period: PopularPeriod,
        remoteSync: Boolean
    ): Flow<Response<List<PopularModel>>> = flow {
        log.v { "getPopularsByTypeStream() called with category: $category | period: $period | remote sync: $remoteSync" }

        emit(Response.Loading)

        if (!remoteSync) {
            emitAll(
                dao.getPopularsStream(category).map {
                    Response.Success(it.toPopularModelList())
                }
            )
        } else {
            when (val response = api.getPopularArticles(category, period)) {
                is ApiResponse.Success -> {
                    log.d { "getPopularsByTypeStream() success: response size: ${response.data.popularResponses.size}" }
                    val populars = response.data.toPopularEntityList(category)
                    dao.insertOrReplace(populars)
                    emitAll(
                        dao.getPopularsStream(category).map {
                            Response.Success(it.toPopularModelList())
                        }
                    )
                }

                is ApiResponse.Failure -> {
                    log.w { "getPopularsByTypeStream() error: ${response.error}" }
                    emitAll(
                        dao.getPopularsStream(category).map {
                            Response.Failure(response.error, it.toPopularModelList())
                        }
                    )
                }
            }
        }
    }
}