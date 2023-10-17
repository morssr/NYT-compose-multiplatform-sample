package com.mls.kmp.mor.nytnewskmp.data.popular.cache

import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import kotlinx.coroutines.flow.Flow

interface PopularDao {
    fun getPopularsStream(category: PopularCategory): Flow<List<PopularEntity>>
    suspend fun insertOrReplace(populars: List<PopularEntity>)
    suspend fun deleteAllPopulars()
}