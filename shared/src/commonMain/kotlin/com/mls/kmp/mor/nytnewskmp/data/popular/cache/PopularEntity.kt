package com.mls.kmp.mor.nytnewskmp.data.popular.cache

import com.mls.kmp.mor.nytnewskmp.data.popular.common.PopularCategory
import kotlinx.datetime.Instant

data class PopularEntity(
    val id: String,
    val category: PopularCategory,
    val title: String,
    val content: String,
    val byline: String,
    val section: String,
    val publishedDate: Instant,
    val imageUrl: String,
    val storyUrl: String
)