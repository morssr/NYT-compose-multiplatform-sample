package com.mls.kmp.mor.nytnewskmp.data.aricles

import kotlinx.datetime.Instant

data class ArticleModel(
    val id: String,
    val subsection: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: Instant,
    val imageUrl: String,
    val storyUrl: String
)