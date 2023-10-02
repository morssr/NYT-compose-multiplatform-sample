package com.mls.kmp.mor.nytnewskmp.ui.bookmarks

import com.mls.kmp.mor.nytnewskmp.data.bookmarks.common.BookmarkModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class BookmarkUiModel(
    val id: String,
    val topic: String,
    val subsection: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: String,
    val bookmarkedDate: String,
    val imageUrl: String,
    val articleUrl: String
)

fun BookmarkModel.toBookmarkUiModel() = BookmarkUiModel(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = publishedDate.toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    bookmarkedDate = bookmarkedDate.toString(),
    imageUrl = imageUrl,
    articleUrl = storyUrl
)

fun List<BookmarkModel>.toBookmarkUiModelsList() = map { it.toBookmarkUiModel() }