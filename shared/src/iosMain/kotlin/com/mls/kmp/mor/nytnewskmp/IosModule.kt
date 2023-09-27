package com.mls.kmp.mor.nytnewskmp

import com.mls.kmp.mor.nytnewskmp.core.data.DatabaseDriverFactory
import com.mls.kmp.mor.nytnewskmp.core.data.createDataStore
import com.mls.kmp.mor.nytnewskmp.data.common.TOPICS_PREFERENCES_FILE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual val platformModule = module {
    single { DatabaseDriverFactory().create() }

    single(qualifier = named(TOPICS_PREFERENCES_FILE_NAME)) {
        createDataStore(coroutineScope = get()) {
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            requireNotNull(documentDirectory).path + "/$TOPICS_PREFERENCES_FILE_NAME"
        }
    } withOptions {
        createdAtStart()
    }
}