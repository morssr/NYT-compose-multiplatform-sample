package com.mls.kmp.mor.nytnewskmp.data.search.cache

import kotlinx.datetime.Instant

data class SearchEntity(
    val id: String,
    val headline: String,
    val content: String,
    val leadParagraph: String,
    val subsection: String?,
    val byline: String,
    val publishedDate: Instant,
    val imageUrl: String,
    val storyUrl: String
)