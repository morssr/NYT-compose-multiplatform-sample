package com.mls.kmp.mor.nytnewskmp.ui.articles

import com.mls.kmp.mor.nytnewskmp.data.aricles.common.ArticleModel

data class ArticleUIModel(
    val id: String,
    val subsection: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: String,
    val imageUrl: String,
    val storyUrl: String,
    val favorite: Boolean
)

fun ArticleModel.toArticleUI(bookmarked: Boolean = false) = ArticleUIModel(
    id = id,
    subsection = subsection,
    title = title,
    content = content,
    byline = byline,
    publishedDate = publishedDate.toString(),
    imageUrl = imageUrl,
    storyUrl = storyUrl,
    favorite = bookmarked
)

fun List<ArticleModel>.toArticleUIList() = map { it.toArticleUI() }