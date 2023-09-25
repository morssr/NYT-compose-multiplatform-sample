package com.mls.kmp.mor.nytnewskmp.utils.api

import com.mls.kmp.mor.nytnewskmp.data.aricles.api.MultimediaResponse

const val IMAGE_NOT_AVAILABLE = "IMAGE_NOT_AVAILABLE"

fun List<MultimediaResponse>?.extractImageUrl(requiredFormat: NytImageFormats): String {
    if (this.isNullOrEmpty()) return IMAGE_NOT_AVAILABLE
    return this.find { it.format == requiredFormat.format }?.url ?: first().url
}

enum class NytImageFormats(val format: String) {
    SUPER_JUMBO("Super Jumbo"),
    THREE_BY_TWO_SMALL("threeByTwoSmallAt2X"),
    LARGE_THUMBNAIL("Large Thumbnail"),
    MEDIUM_THREE_BY_TWO_440("mediumThreeByTwo440"),
    MEDIUM_THREE_BY_TWO_220("mediumThreeByTwo210"),
    STANDARD_THUMBNAIL("Standard Thumbnail");

    override fun toString(): String {
        return format
    }
}