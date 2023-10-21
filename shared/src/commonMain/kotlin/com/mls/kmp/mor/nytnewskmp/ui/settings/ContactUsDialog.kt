package com.mls.kmp.mor.nytnewskmp.ui.settings

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.MyLocation
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mls.kmp.mor.nytnewskmp.library.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun ContactUsDialog(
    modifier: Modifier = Modifier,
    onEmailClick: () -> Unit = { },
    onDismiss: () -> Unit = { },
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(MR.strings.contact_information),
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clickable { onEmailClick() },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = stringResource(MR.strings.email)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = " ${stringResource(MR.strings.app_contact_email_address)}",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MyLocation,
                        contentDescription = stringResource(MR.strings.location)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = " ${stringResource(MR.strings.app_information_location)}",
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}