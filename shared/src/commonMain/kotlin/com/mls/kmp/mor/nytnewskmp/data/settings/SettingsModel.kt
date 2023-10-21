package com.mls.kmp.mor.nytnewskmp.data.settings

import com.mls.kmp.mor.nytnewskmp.ui.settings.ThemeConfig

data class SettingsModel(
    val theme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
    val showDisclaimer: Boolean = false
)