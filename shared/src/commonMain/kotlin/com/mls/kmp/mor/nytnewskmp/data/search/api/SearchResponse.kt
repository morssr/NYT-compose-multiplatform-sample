package com.mls.kmp.mor.nytnewskmp.data.search.api

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val status: String,
    val response: SearchArticlesResponse
)

@Serializable
data class SearchArticlesResponse(
    val docs: List<SearchArticleResponse>
)

@Serializable
data class SearchArticleResponse(
    val _id: String,
    val abstract: String,
    val byline: Byline,
    val document_type: String,
    val headline: Headline,
    val print_section: String,
    val multimedia: Multimedia?,
    val pub_date: String,
    val section_name: String,
    val snippet: String,
    val source: String,
    val subsection_name: String? = null,
    val uri: String,
    val web_url: String,
)

@Serializable
data class Headline(
    val main: String
)

@Serializable
data class Multimedia(
    val caption: String,
    val credit: String,
    val default: Media,
    val thumbnail: Media
)

@Serializable
data class Media(
    val url: String,
    val height: Int,
    val width: Int
)

@Serializable
data class Byline(
    val original: String = "",
)

@Serializable
data class Meta(
    val hits: Int,
    val offset: Int,
    val time: Int
)