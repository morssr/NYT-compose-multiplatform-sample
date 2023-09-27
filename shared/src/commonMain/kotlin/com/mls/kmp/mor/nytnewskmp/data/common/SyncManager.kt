package com.mls.kmp.mor.nytnewskmp.data.common

interface SyncManager {
    suspend fun isSyncNeeded(topic: Topics): Boolean
    suspend fun updateLastSyncTimestamp(topic: Topics)
}