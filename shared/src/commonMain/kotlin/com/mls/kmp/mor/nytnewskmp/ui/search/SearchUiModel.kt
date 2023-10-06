package com.mls.kmp.mor.nytnewskmp.ui.search

import com.mls.kmp.mor.nytnewskmp.data.search.SearchModel
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class SearchUiModel(
    val id: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: String,
    val imageUrl: String,
    val articleUrl: String
)

fun SearchModel.toSearchUiModel() = SearchUiModel(
    id = id,
    title = headline,
    content = content,
    byline = byline,
    publishedDate = Instant.fromEpochMilliseconds(publishedDate)
        .toLocalDateTime(TimeZone.currentSystemDefault()).toString(),
    imageUrl = imageUrl,
    articleUrl = articleUrl
)

fun List<SearchModel>.toSearchUiModels() = map { it.toSearchUiModel() }

fun SearchUiModel.toSearchModel() = SearchModel(
    id = id,
    headline = title,
    content = content,
    leadParagraph = "",
    subsection = "",
    byline = byline,
    publishedDate = LocalDateTime.parse(publishedDate).toInstant(TimeZone.currentSystemDefault())
        .toEpochMilliseconds(),
    imageUrl = imageUrl,
    articleUrl = articleUrl
)