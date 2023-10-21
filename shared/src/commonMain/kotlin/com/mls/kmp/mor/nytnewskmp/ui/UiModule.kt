package com.mls.kmp.mor.nytnewskmp.ui

import com.mls.kmp.mor.nytnewskmp.ui.bookmarks.BookmarksScreenViewModel
import com.mls.kmp.mor.nytnewskmp.ui.search.SearchViewModel
import com.mls.kmp.mor.nytnewskmp.ui.settings.SettingsViewModel
import org.koin.dsl.module

val uiModule = module {
    single {
        HomeScreenViewModel(
            repository = get(),
            bookmarksRepository = get(),
            popularsRepository = get(),
            logger = get(),
        )
    }

    single {
        BookmarksScreenViewModel(
            repository = get(),
            logger = get(),
        )
    }

    factory {
        SearchViewModel(
            repository = get(),
            logger = get(),
        )
    }

    single {
        SettingsViewModel(
            settingsRepository = get(),
            logger = get(),
        )
    }
}