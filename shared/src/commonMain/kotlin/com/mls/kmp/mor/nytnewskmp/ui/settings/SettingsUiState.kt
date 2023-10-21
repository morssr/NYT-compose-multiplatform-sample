package com.mls.kmp.mor.nytnewskmp.ui.settings

import com.mls.kmp.mor.nytnewskmp.data.settings.SettingsModel

data class SettingsUiState(
    val theme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
    val showDisclaimer: Boolean = false,
    val dialogSelector: DialogSelector = DialogSelector.None
)

fun SettingsModel.toUiState(oldStata: SettingsUiState): SettingsUiState {
    return SettingsUiState(
        theme = theme,
        showDisclaimer = showDisclaimer,
        dialogSelector = oldStata.dialogSelector
    )
}