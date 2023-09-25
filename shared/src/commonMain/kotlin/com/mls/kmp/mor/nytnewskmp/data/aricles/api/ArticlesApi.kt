package com.mls.kmp.mor.nytnewskmp.data.aricles.api

import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.utils.ApiResponse

interface ArticlesApi {
    suspend fun getTopics(topic: Topics): ApiResponse<TopicsResponse>
}