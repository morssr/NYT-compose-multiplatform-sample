package com.mls.kmp.mor.nytnewskmp.data.popular.common

import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularModel
import com.mls.kmp.mor.nytnewskmp.utils.Response
import kotlinx.coroutines.flow.Flow

interface PopularRepository {

    fun getPopularsByTypeStream(
        category: PopularCategory = PopularCategory.MOST_VIEWED,
        period: PopularPeriod = PopularPeriod.DAY,
        remoteSync: Boolean = true
    ): Flow<Response<List<PopularModel>>>
}