package com.mls.kmp.mor.nytnewskmp.ui.populars

import com.mls.kmp.mor.nytnewskmp.data.popular.cache.PopularModel

data class PopularUiModel(
    val id: String,
    val title: String,
    val content: String,
    val byline: String,
    val publishedDate: String,
    val imageUrl: String,
    val storyUrl: String,
)

fun PopularModel.toPopularUi(): PopularUiModel {
    return PopularUiModel(
        id = id,
        title = title,
        content = content,
        byline = byline,
        publishedDate = publishedDate.toString(),
        imageUrl = imageUrl,
        storyUrl = storyUrl
    )
}

fun List<PopularModel>.toPopularUiList(): List<PopularUiModel> {
    return map { it.toPopularUi() }
}