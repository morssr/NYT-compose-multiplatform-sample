package com.mls.kmp.mor.nytnewskmp.data.aricles.common

import com.mls.kmp.mor.nytnewskmp.data.aricles.api.TopicResponse
import com.mls.kmp.mor.nytnewskmp.data.aricles.cache.ArticleEntity
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import com.mls.kmp.mor.nytnewskmp.utils.api.NytImageFormats
import com.mls.kmp.mor.nytnewskmp.utils.api.extractImageUrl
import database.Articles
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant

fun TopicResponse.toArticleEntity(topic: Topics) = ArticleEntity(
    id = uri,
    topic = topic.topicName.lowercase(),
    subsection = subsection,
    title = title,
    content = abstract,
    byline = byline,
    itemType = item_type,
    publishedDate = published_date.toInstant().toEpochMilliseconds(),
    createdDate = created_date.toInstant().toEpochMilliseconds(),
    updatedDate = updated_date.toInstant().toEpochMilliseconds(),
    imageUrl = multimedia.extractImageUrl(NytImageFormats.THREE_BY_TWO_SMALL),
    storyUrl = url
)

fun List<TopicResponse>.toArticleEntityList(topic: Topics) = map { it.toArticleEntity(topic) }

fun ArticleEntity.toArticleDbObject() = Articles(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    item_type = itemType,
    published_date = publishedDate,
    created_date = createdDate,
    updated_date = updatedDate,
    image_url = imageUrl,
    story_url = storyUrl
)

fun Articles.toArticleEntity() = ArticleEntity(
    id = id,
    topic = topic,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    itemType = item_type,
    publishedDate = published_date,
    createdDate = created_date,
    updatedDate = updated_date,
    imageUrl = image_url,
    storyUrl = story_url
)

fun List<Articles>.toArticleEntityList() = map { it.toArticleEntity() }

fun ArticleEntity.toArticleModel() = ArticleModel(
    id = id,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = Instant.fromEpochMilliseconds(publishedDate),
    imageUrl = imageUrl,
    storyUrl = storyUrl
)

fun List<ArticleEntity>.toArticleModelList() = map { it.toArticleModel() }