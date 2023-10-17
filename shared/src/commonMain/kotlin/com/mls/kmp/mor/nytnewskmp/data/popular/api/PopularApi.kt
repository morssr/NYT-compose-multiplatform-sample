package com.mls.kmp.mor.nytnewskmp.data.popular.api

import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularPeriod
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse

interface PopularApi {
    suspend fun getPopularArticles(
        category: PopularCategory,
        period: PopularPeriod
    ): ApiResponse<PopularsResponse>
}