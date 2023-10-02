package com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache

import kotlinx.datetime.Instant

data class BookmarkEntity(
    val id: String,
    val topic: String,
    val subsection: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: Instant,
    val bookmarkedDate: Instant,
    val imageUrl: String,
    val storyUrl: String
)