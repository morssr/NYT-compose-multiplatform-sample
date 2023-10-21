package com.mls.kmp.mor.nytnewskmp

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import com.mls.kmp.mor.nytnewskmp.core.api.ApiClient
import com.mls.kmp.mor.nytnewskmp.data.aricles.articlesModule
import com.mls.kmp.mor.nytnewskmp.data.bookmarks.bookmarksModule
import com.mls.kmp.mor.nytnewskmp.data.popular.popularsModule
import com.mls.kmp.mor.nytnewskmp.data.search.searchModule
import com.mls.kmp.mor.nytnewskmp.data.settings.settingsModule
import com.mls.kmp.mor.nytnewskmp.database.AppDatabase
import com.mls.kmp.mor.nytnewskmp.ui.uiModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import org.koin.core.module.Module
import org.koin.dsl.module

val mainModule = module {

    //TODO: Check if this is the best way to provide CoroutineScope
    single<CoroutineScope> { GlobalScope }

    single { ApiClient() }
    single { AppDatabase(driver = get()) }

    val baseLogger = Logger(
        config = StaticConfig(
            logWriterList = listOf(platformLogWriter()),
            minSeverity = Severity.Verbose
        ), "MyAppTag"
    )

    factory { baseLogger }
}

val appModule = module {
    includes(
        mainModule,
        platformModule,
        uiModule,
        articlesModule,
        bookmarksModule,
        searchModule,
        popularsModule,
        settingsModule
    )
}
expect val platformModule: Module