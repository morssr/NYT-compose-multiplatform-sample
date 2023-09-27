package com.mls.kmp.mor.nytnewskmp

import com.mls.kmp.mor.nytnewskmp.core.data.DatabaseDriverFactory
import com.mls.kmp.mor.nytnewskmp.core.data.createDataStore
import com.mls.kmp.mor.nytnewskmp.data.common.TOPICS_PREFERENCES_FILE_NAME
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val platformModule = module {

    single { DatabaseDriverFactory(context = androidContext()).create() }

    single(qualifier = named(TOPICS_PREFERENCES_FILE_NAME)) {
        createDataStore(coroutineScope = get()) {
            androidContext().filesDir.resolve(
                TOPICS_PREFERENCES_FILE_NAME
            ).absolutePath
        }
    } withOptions {
        createdAtStart()
    }
}
