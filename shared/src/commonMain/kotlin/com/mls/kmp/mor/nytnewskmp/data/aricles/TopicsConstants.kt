package com.mls.kmp.mor.nytnewskmp.data.aricles

import com.mls.kmp.mor.nytnewskmp.data.common.Topics

const val TOPICS_PREFERENCES_FILE_NAME = "nyt_topics.preferences_pb"
const val FAVORITE_TOPICS_PREFERENCES_KEY = "favorite_topics"
const val TOPICS_LAST_UPDATE_PREFERENCES_FILE_NAME = "topics_last_update_pref"

val defaultTopics =
    listOf(Topics.HOME, Topics.TECHNOLOGY, Topics.POLITICS, Topics.SPORTS)
