package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NetworkCheck
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GenericErrorScreen(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.NetworkCheck,
    title: String = "No Internet Connection",
    description: String = "Please check your internet connection and try again.",
    actionButtonText: String = "Try Again",
    onActionButtonClick: () -> Unit = {},
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier
                    .sizeIn(
                        minWidth = 64.dp,
                        minHeight = 64.dp,
                        maxWidth = 192.dp,
                        maxHeight = 192.dp
                    )
                    .weight(0.4f)
                    .aspectRatio(1f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error,
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onActionButtonClick,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Text(
                    text = actionButtonText,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}