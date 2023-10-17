package com.mls.kmp.mor.nytnewskmp.data.popular.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PopularsResponse(
    val num_results: Int,
    @SerialName("results")
    val popularResponses: List<PopularResponse>,
    val status: String
)

@Serializable
data class PopularResponse(
    val id: Long,
    val title: String,
    val abstract: String,
    val byline: String,
    val media: List<Media>?,
    val published_date: String,
    val section: String,
    val subsection: String?,
    val uri: String,
    val url: String
)

@Serializable
data class Media(
    @SerialName("media-metadata")
    val media_metadata: List<MediaMetadata>
)

@Serializable
data class MediaMetadata(
    val format: String,
    val height: Int,
    val url: String,
    val width: Int
)