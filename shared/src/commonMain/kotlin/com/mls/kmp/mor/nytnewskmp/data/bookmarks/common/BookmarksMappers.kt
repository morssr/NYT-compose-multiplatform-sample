package com.mls.kmp.mor.nytnewskmp.data.bookmarks.common

import com.mls.kmp.mor.nytnewskmp.data.aricles.common.ArticleModel
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.cache.BookmarkEntity
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import database.Bookmarks
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

fun Bookmarks.toBookmarkEntity() = BookmarkEntity(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = Instant.fromEpochSeconds(published_date),
    bookmarkedDate = Instant.fromEpochSeconds(bookmarked_date),
    imageUrl = image_url,
    storyUrl = story_url
)

fun List<Bookmarks>.toBookmarksEntitiesList() = map { it.toBookmarkEntity() }

fun BookmarkEntity.toBookmarkDbObject() = Bookmarks(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    published_date = publishedDate.epochSeconds,
    bookmarked_date = bookmarkedDate.epochSeconds,
    image_url = imageUrl,
    story_url = storyUrl
)

fun BookmarkEntity.toBookmarkModel() = BookmarkModel(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = publishedDate,
    bookmarkedDate = bookmarkedDate,
    imageUrl = imageUrl,
    storyUrl = storyUrl
)

fun List<BookmarkEntity>.toBookmarksModelList() = map { it.toBookmarkModel() }

fun BookmarkModel.toBookmarkEntity() = BookmarkEntity(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = publishedDate,
    bookmarkedDate = bookmarkedDate,
    imageUrl = imageUrl,
    storyUrl = storyUrl
)

fun ArticleModel.toBookmarkModel(topics: Topics) = BookmarkModel(
    id = id,
    topic = topics.name,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = publishedDate,
    bookmarkedDate = Clock.System.now(),
    imageUrl = imageUrl,
    storyUrl = storyUrl
)