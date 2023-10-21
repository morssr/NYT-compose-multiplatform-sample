package com.mls.kmp.mor.nytnewskmp.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Light
import androidx.compose.material.icons.filled.SettingsApplications
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mls.kmp.mor.nytnewskmp.library.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun AppSettingsDialog(
    modifier: Modifier = Modifier,
    state: SettingsUiState,
    onThemeChanged: (ThemeConfig) -> Unit = { },
    onDismiss: () -> Unit = { },
) {

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = modifier, shape = RoundedCornerShape(16.dp)) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))

                SettingsDialogContent(
                    currentTheme = state.theme,
                    onThemeChanged = { onThemeChanged(it) },
                )

                TextButton(modifier = Modifier.fillMaxWidth(), onClick = { onDismiss() }) {
                    Text(text = stringResource(MR.strings.done))
                }
            }
        }
    }
}

@Composable
fun SettingsDialogContent(
    modifier: Modifier = Modifier,
    currentTheme: ThemeConfig = ThemeConfig.FOLLOW_SYSTEM,
    onThemeChanged: (ThemeConfig) -> Unit = { },
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(text = stringResource(MR.strings.theme_modes))

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ThemeConfig.values().forEach { themeConfig ->
                FilledIconToggleButton(
                    modifier = Modifier.fillMaxWidth(0.75f),
                    checked = themeConfig == currentTheme,
                    onCheckedChange = { onThemeChanged(themeConfig) }) {
                    val text = when (themeConfig) {
                        ThemeConfig.FOLLOW_SYSTEM -> stringResource(MR.strings.follow_system)
                        ThemeConfig.LIGHT -> stringResource(MR.strings.light)
                        ThemeConfig.DARK -> stringResource(MR.strings.dark)
                    }

                    val icon = when (themeConfig) {
                        ThemeConfig.FOLLOW_SYSTEM -> Icons.Default.SettingsApplications
                        ThemeConfig.LIGHT -> Icons.Default.Light
                        ThemeConfig.DARK -> Icons.Default.DarkMode
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(text = text)
                        Icon(imageVector = icon, contentDescription = text)
                    }
                }
            }
        }
    }
}


@Composable
fun SettingsDialogsPresenter(
    state: SettingsUiState = SettingsUiState(),
    onShowDialogClick: (DialogSelector) -> Unit = {},
    onDismiss: () -> Unit = {},
    onThemeChanged: (ThemeConfig) -> Unit = { },
) {
    when (state.dialogSelector) {
        DialogSelector.AboutUs -> {
            AboutUsDialog(onDismiss = { onDismiss() })
        }

        DialogSelector.ContactUs -> {
            ContactUsDialog(
                onDismiss = { onDismiss() },
                onEmailClick = {
                    onShowDialogClick(DialogSelector.EmailChooser)
                },
            )
        }

        DialogSelector.Settings -> {
            AppSettingsDialog(
                state = state,
                onThemeChanged = onThemeChanged,
                onDismiss = { onDismiss() })
        }

//        DialogSelector.EmailChooser -> {
//            EmailChooserMenu(recipient = stringResource(MR.strings.app_contact_email_address))
//            onShowDialogClick(DialogSelector.None)
//        }

        else -> {}
    }

}