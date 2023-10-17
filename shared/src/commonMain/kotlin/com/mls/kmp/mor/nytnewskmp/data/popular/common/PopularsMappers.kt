package com.mls.kmp.mor.nytnewskmp.data.popular.common

import com.mls.kmp.mor.nytnewskmp.data.popular.api.Media
import com.mls.kmp.mor.nytnewskmp.data.popular.api.PopularResponse
import com.mls.kmp.mor.nytnewskmp.data.popular.api.PopularsResponse
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularEntity
import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularModel
import com.mls.kmp.mor.nytnewskmp.utils.NO_IMAGES
import database.Populars
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDate

fun PopularResponse.toPopularEntity(category: PopularCategory): PopularEntity {
    return PopularEntity(
        id = uri,
        category = category,
        title = title,
        content = abstract,
        byline = byline,
        section = section,
        publishedDate = published_date.toLocalDate()
            .atStartOfDayIn(TimeZone.currentSystemDefault()),
        imageUrl = media?.extractPopularImageUrl(PopularImageFormat.MEDIUM_THREE_BY_TWO_440)
            ?: NO_IMAGES,
        storyUrl = url
    )
}

fun PopularsResponse.toPopularEntityList(category: PopularCategory): List<PopularEntity> {
    return popularResponses.map { it.toPopularEntity(category) }
}

fun Populars.toPopularEntity(): PopularEntity {
    return PopularEntity(
        id = id,
        category = PopularCategory.fromString(category),
        title = title,
        content = content,
        byline = byline,
        section = section,
        publishedDate = Instant.fromEpochMilliseconds(published_date),
        imageUrl = image_url,
        storyUrl = story_url
    )
}

fun List<Populars>.toPopularEntityList(): List<PopularEntity> {
    return map { it.toPopularEntity() }
}

fun PopularEntity.toPopularDbObject(): Populars {
    return Populars(
        id = id,
        category = category.toString(),
        title = title,
        content = content,
        byline = byline,
        section = section,
        published_date = publishedDate.toEpochMilliseconds(),
        image_url = imageUrl,
        story_url = storyUrl
    )
}

fun PopularEntity.toPopularModel(): PopularModel {
    return PopularModel(
        id = id,
        title = title,
        content = content,
        byline = byline,
        section = section,
        publishedDate = publishedDate,
        imageUrl = imageUrl,
        storyUrl = storyUrl
    )
}

fun List<PopularEntity>.toPopularModelList(): List<PopularModel> {
    return map { it.toPopularModel() }
}

private fun List<Media>.extractPopularImageUrl(imageFormat: PopularImageFormat): String {
    return try {
        map { it.media_metadata }
            .map {
                it.find { mediaMetadata -> mediaMetadata.format == imageFormat.format }?.url
                    ?: NO_IMAGES
            }
            .firstOrNull() ?: NO_IMAGES
    } catch (e: Exception) {
        NO_IMAGES
    }
}

private enum class PopularImageFormat(val format: String) {
    MEDIUM_THREE_BY_TWO_440("mediumThreeByTwo440"),
    MEDIUM_THREE_BY_TWO_220("mediumThreeByTwo210"),
    STANDARD_THUMBNAIL("Standard Thumbnail");

    override fun toString(): String {
        return format
    }
}