package com.mls.kmp.mor.nytnewskmp.ui.common

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
actual fun OpenNTYWebpageButton(url: String, title: String) {
    val context = LocalContext.current

    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { linkToWebpage(url, context) }
    ) {
        Text(text = title)

        Spacer(modifier = Modifier.width(16.dp))

        Icon(imageVector = Icons.Outlined.Web, contentDescription = null)
    }
}

fun linkToWebpage(url: String, context: Context) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse(url)
    ContextCompat.startActivity(context, openURL, null)
}