package com.mls.kmp.mor.nytnewskmp.data.aricles

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.common.SyncManager
import com.mls.kmp.mor.nytnewskmp.data.common.Topics
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

private const val TAG = "ArticlesSyncManager"

class ArticlesSyncManager(
    private val preferences: DataStore<Preferences>,
    private val timeIntervalMinutes: Int = 20,
    logger: Logger
) : SyncManager {

    private val log = logger.withTag(TAG)

    override suspend fun isSyncNeeded(topic: Topics): Boolean {
        log.v { "isSyncNeeded() called with topic: $topic" }
        val lastSyncTimestamp = getLastSyncTimestamp(topic)
        val timeDifference = getCurrentTime() - lastSyncTimestamp
        val timeDifferenceInMinutes = timeDifference / 1000 / 60
        return timeDifferenceInMinutes >= timeIntervalMinutes
    }

    private suspend fun getLastSyncTimestamp(topic: Topics): Long {
        log.v { "getLastSyncTimestamp() called with topic: $topic" }
        return preferences.data.map { it[longPreferencesKey(topic.topicName)] ?: 0L }.first()
    }

    override suspend fun updateLastSyncTimestamp(
        topic: Topics
    ) {
        log.v { "updateLastSyncTimestamp() called with topic: $topic" }
        preferences.edit {
            it[longPreferencesKey(topic.topicName)] = getCurrentTime()
        }
    }

    private fun getCurrentTime() = Clock.System.now().toEpochMilliseconds()
}