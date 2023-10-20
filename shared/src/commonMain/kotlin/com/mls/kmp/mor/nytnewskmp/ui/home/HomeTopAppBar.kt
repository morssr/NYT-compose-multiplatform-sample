package com.mls.kmp.mor.nytnewskmp.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContactSupport
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mls.kmp.mor.nytnewskmp.library.MR
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource


@Composable
fun HomeTopAppBar(
    modifier: Modifier = Modifier,
    showMainMenu: Boolean = false,
    isDarkMode: Boolean = false,
    onDismissMenu: () -> Unit = {},
    onLogoClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onSettingClick: () -> Unit = {},
    onAboutUsClick: () -> Unit = {},
    onContactUsClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, start = 8.dp, end = 8.dp)
    ) {
        IconButton(modifier = Modifier.align(Alignment.CenterStart), onClick = onLogoClick) {
            Image(
                painter = painterResource(if (isDarkMode) MR.images.rounded_logo_dark_svg else MR.images.rounded_logo_light_svg),
                contentDescription = stringResource(MR.strings.app_toolbar_logo_and_about_content_description),
            )
        }

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = stringResource(MR.strings.app_main_menu_content_description)
                )
            }

            DropdownMenu(expanded = showMainMenu, onDismissRequest = onDismissMenu) {

                DropdownMenuItem(
                    text = { Text(text = stringResource(MR.strings.settings)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = stringResource(MR.strings.app_main_menu_settings_content_description)
                        )
                    },
                    onClick = {
                        onDismissMenu()
                        onSettingClick()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(MR.strings.about)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(MR.strings.app_main_menu_about_us_content_description)
                        )
                    },
                    onClick = {
                        onDismissMenu()
                        onAboutUsClick()
                    },
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(MR.strings.contact_us)) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.ContactSupport,
                            contentDescription = stringResource(MR.strings.app_main_menu_contact_us_content_description)
                        )
                    },
                    onClick = {
                        onDismissMenu()
                        onContactUsClick()
                    },
                )
            }
        }
    }
}