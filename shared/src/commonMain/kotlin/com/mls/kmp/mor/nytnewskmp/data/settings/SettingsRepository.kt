package com.mls.kmp.mor.nytnewskmp.data.settings

import com.mls.kmp.mor.nytnewskmp.ui.settings.ThemeConfig
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<SettingsModel>
    suspend fun getTheme(): ThemeConfig
    suspend fun setTheme(theme: ThemeConfig)
    suspend fun getShowDisclaimer(): Boolean
    suspend fun setShowDisclaimer(show: Boolean)
}