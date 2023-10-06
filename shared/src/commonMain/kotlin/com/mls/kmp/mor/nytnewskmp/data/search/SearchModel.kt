package com.mls.kmp.mor.nytnewskmp.data.search

data class SearchModel(
    val id: String,
    val headline: String,
    val content: String,
    val leadParagraph: String,
    val subsection: String?,
    val byline: String,
    val publishedDate: Long,
    val imageUrl: String,
    val articleUrl: String
)