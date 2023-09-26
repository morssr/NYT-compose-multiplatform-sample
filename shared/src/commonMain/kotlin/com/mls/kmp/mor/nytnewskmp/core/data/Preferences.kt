package com.mls.kmp.mor.nytnewskmp.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.CoroutineScope
import okio.Path.Companion.toPath

fun createDataStore(
    coroutineScope: CoroutineScope,
    producePath: () -> String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = coroutineScope,
        produceFile = { producePath().toPath() },
    )
}

internal const val globalDataStoreFileName = "nyt_global.preferences_pb"
