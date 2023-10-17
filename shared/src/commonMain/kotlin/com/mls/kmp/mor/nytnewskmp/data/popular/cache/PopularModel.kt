package com.mls.kmp.mor.nytnewskmp.data.popular.cache

import kotlinx.datetime.Instant

data class PopularModel(
    val id: String,
    val title: String,
    val content: String,
    val byline: String,
    val section: String,
    val publishedDate: Instant,
    val imageUrl: String,
    val storyUrl: String
)