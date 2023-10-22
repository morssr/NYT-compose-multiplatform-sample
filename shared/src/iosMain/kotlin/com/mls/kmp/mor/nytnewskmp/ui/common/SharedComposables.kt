package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun OpenNTYWebpageButton(url: String, title: String) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { println(url) }
    ) {
        Text(text = title)

        Spacer(modifier = Modifier.width(16.dp))

        Icon(imageVector = Icons.Outlined.Web, contentDescription = null)
    }
}