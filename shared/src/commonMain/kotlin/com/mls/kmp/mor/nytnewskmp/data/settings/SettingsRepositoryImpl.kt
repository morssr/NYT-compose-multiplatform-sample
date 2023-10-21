package com.mls.kmp.mor.nytnewskmp.data.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.ui.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val TAG = "SettingsRepositoryImpl"

class SettingsRepositoryImpl(
    private val settingsPreferences: DataStore<Preferences>,
    logger: Logger
): SettingsRepository {
    private val log = logger.withTag(TAG)


    override fun getSettings(): Flow<SettingsModel> {
        log.d { "getSettings(): called" }
        return settingsPreferences.data.map { preferences ->
            SettingsModel(
                theme = preferences[stringPreferencesKey(THEME_MODE_PREFERENCES_KEY)]?.let {
                    ThemeConfig.valueOf(
                        it
                    )
                } ?: ThemeConfig.FOLLOW_SYSTEM,

                showDisclaimer = preferences[booleanPreferencesKey(
                    SHOW_DISCLAIMER_PREFERENCES_KEY
                )] ?: true
            )
        }
    }

    override suspend fun getTheme(): ThemeConfig {
        log.d { "getTheme(): called" }
        return settingsPreferences.data.map { preferences ->
            preferences[stringPreferencesKey(THEME_MODE_PREFERENCES_KEY)]?.let {
                ThemeConfig.valueOf(
                    it
                )
            }
                ?: ThemeConfig.FOLLOW_SYSTEM
        }.first()
    }

    override suspend fun setTheme(theme: ThemeConfig) {
        log.d { "setTheme(): called with theme: $theme" }
        settingsPreferences.edit { preferences ->
            preferences[stringPreferencesKey(THEME_MODE_PREFERENCES_KEY)] = theme.name
        }
    }

    override suspend fun getShowDisclaimer(): Boolean {
        log.d { "getShowDisclaimer(): called" }
        return settingsPreferences.data.map { preferences ->
            preferences[booleanPreferencesKey(SHOW_DISCLAIMER_PREFERENCES_KEY)] ?: false
        }.first()
    }

    override suspend fun setShowDisclaimer(show: Boolean) {
        log.d { "setShowDisclaimer(): called with show: $show" }
        settingsPreferences.edit { preferences ->
            preferences[booleanPreferencesKey(SHOW_DISCLAIMER_PREFERENCES_KEY)] =
                show
        }
    }
}