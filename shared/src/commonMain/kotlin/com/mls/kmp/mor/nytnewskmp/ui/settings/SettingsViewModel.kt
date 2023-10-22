package com.mls.kmp.mor.nytnewskmp.ui.settings

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import co.touchlab.kermit.Logger
import com.mls.kmp.mor.nytnewskmp.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "SettingsViewModel"

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    logger: Logger
) : StateScreenModel<SettingsUiState>(SettingsUiState()) {

    private val log = logger.withTag(TAG)

    init {
        settingsRepository.getSettings()
            .onEach { settingsUiState ->
                mutableState.update { settingsUiState.toUiState(it) }
            }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.Lazily,
                initialValue = SettingsUiState()
            )
            .launchIn(coroutineScope)
    }

    fun onThemeChanged(theme: ThemeConfig) {
        log.d { "onThemeChanged(): called with theme: $theme" }
        coroutineScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun dismissDisclaimer() {
        log.d { "dismissDisclaimer(): called" }
        mutableState.update { it.copy(showDisclaimer = false) }
    }

    fun setShowDisclaimer(show: Boolean) {
        log.d { "onShowDisclaimerChanged(): called with show: $show" }
        coroutineScope.launch {
            settingsRepository.setShowDisclaimer(show)
        }
    }

    fun showDialog(dialogSelector: DialogSelector) {
        log.d { "showDialog() called with: dialogSelector = $dialogSelector" }
        mutableState.update { it.copy(dialogSelector = dialogSelector) }
    }

    fun dismissDialog() {
        log.d { "dismissDialog() called" }
        mutableState.update { it.copy(dialogSelector = DialogSelector.None) }
    }
}

sealed class DialogSelector {
    object MainMenuDropdown : DialogSelector()
    object Settings : DialogSelector()
    object AboutUs : DialogSelector()
    object ContactUs : DialogSelector()
    object EmailChooser : DialogSelector()
    object None : DialogSelector()
}
