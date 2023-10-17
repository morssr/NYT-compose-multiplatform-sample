package com.mls.kmp.mor.nytnewskmp.data.popular.common

enum class PopularCategory(private val category: String) {
    MOST_EMAILED("emailed"),
    MOST_SHARED("shared"),
    MOST_VIEWED("viewed");

    override fun toString() = category

    companion object {
        fun fromString(category: String): PopularCategory {
            return when (category) {
                "emailed" -> MOST_EMAILED
                "shared" -> MOST_SHARED
                "viewed" -> MOST_VIEWED
                else -> throw IllegalArgumentException("Unknown category: $category")
            }
        }
    }
}