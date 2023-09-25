package com.mls.kmp.mor.nytnewskmp.data.aricles.cache

data class ArticleEntity(
    val id: String,
    val topic: String,
    val subsection: String,
    val title: String,
    val content: String,
    val byline: String,
    val itemType: String,
    val publishedDate: Long,
    val createdDate: Long,
    val updatedDate: Long,
    val imageUrl: String,
    val storyUrl: String
)