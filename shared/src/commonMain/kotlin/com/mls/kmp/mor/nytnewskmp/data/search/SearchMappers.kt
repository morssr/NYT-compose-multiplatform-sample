package com.mls.kmp.mor.nytnewskmp.data.search

import com.mls.kmp.mor.nytnewskmp.data.aricles.common.ArticleModel
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarkEntity
import com.mls.kmp.mor.nytnewskmp.data.search.api.SearchArticleResponse
import com.mls.kmp.mor.nytnewskmp.data.search.cache.SearchEntity
import database.Search
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

private const val IMAGE_PREFIX = "https://static01.nyt.com/"

fun SearchArticleResponse.toSearchEntity(): SearchEntity {
    return SearchEntity(
        id = _id,
        headline = headline.main,
        content = abstract,
        leadParagraph = lead_paragraph,
        subsection = subsection_name,
        byline = byline.original,
        publishedDate = parseSearchApiResponseDate(),
        imageUrl = extractImageFromResponse(),
        storyUrl = web_url
    )
}

fun List<SearchArticleResponse>.toSearchEntities(): List<SearchEntity> {
    return map { it.toSearchEntity() }
}

private fun SearchArticleResponse.parseSearchApiResponseDate(): Instant {
    return try {
        //remove redundant suffix to fit the iso format
        val cleanDate = pub_date.removeSuffix("+0000")
        val parsed = LocalDateTime.parse(cleanDate)
        parsed.toInstant(TimeZone.currentSystemDefault())
    } catch (e: Exception) {
        println("exception: $e")
        Clock.System.now()
    }
}

private fun SearchArticleResponse.extractImageFromResponse() =
    multimedia?.find { it.height in 300..500 }?.url?.let { IMAGE_PREFIX + it } ?: ""


fun Search.toSearchEntity(): SearchEntity {
    return SearchEntity(
        id = id,
        headline = title,
        content = content,
        leadParagraph = lead_paragraph,
        subsection = subsection,
        byline = byline,
        publishedDate = Instant.fromEpochSeconds(published_date),
        imageUrl = image_url,
        storyUrl = story_url
    )
}

fun List<Search>.fromSearchDbToSearchEntities(): List<SearchEntity> {
    return map { it.toSearchEntity() }
}

fun SearchEntity.toSearchDbObject(): Search {
    return Search(
        id = id,
        title = headline,
        content = content,
        lead_paragraph = leadParagraph,
        subsection = subsection,
        byline = byline,
        published_date = publishedDate.toEpochMilliseconds(),
        image_url = imageUrl,
        story_url = storyUrl
    )
}

fun SearchEntity.toSearchModel() = SearchModel(
    id = id,
    headline = headline,
    content = content,
    leadParagraph = leadParagraph,
    subsection = subsection,
    byline = byline,
    publishedDate = publishedDate.toEpochMilliseconds(),
    imageUrl = imageUrl,
    articleUrl = storyUrl
)

fun List<SearchEntity>.toSearchModels(): List<SearchModel> {
    return map { it.toSearchModel() }
}

fun ArticleModel.articleModelToSearchModel(): SearchModel {
    return SearchModel(
        id = id,
        headline = title,
        content = content,
        leadParagraph = content,
        subsection = subsection,
        byline = byline,
        publishedDate = publishedDate.toEpochMilliseconds(),
        imageUrl = imageUrl,
        articleUrl = storyUrl
    )
}

fun List<ArticleModel>.fromArticleToSearchModels(): List<SearchModel> {
    return map { it.articleModelToSearchModel() }
}

fun SearchModel.toBookmarkEntity(): BookmarkEntity {
    return BookmarkEntity(
        id = id,
        topic = subsection ?: "",
        title = headline,
        content = content,
        subsection = subsection ?: "",
        byline = byline,
        publishedDate = Instant.fromEpochMilliseconds(publishedDate),
        bookmarkedDate = Clock.System.now(),
        imageUrl = imageUrl,
        storyUrl = articleUrl
    )
}