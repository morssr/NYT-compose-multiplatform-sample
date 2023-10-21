package com.mls.kmp.mor.nytnewskmp.data.settings

import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {

    single<SettingsRepository> {
        SettingsRepositoryImpl(
            settingsPreferences = get(qualifier = named(SETTINGS_PREFERENCES_FILE_NAME)),
            logger = get(),
        )
    }
}