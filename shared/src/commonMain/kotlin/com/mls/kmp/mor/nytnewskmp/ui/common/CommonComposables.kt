package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material.icons.rounded.NetworkCheck
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import com.mls.kmp.mor.nytnewskmp.library.MR
import com.mls.kmp.mor.nytnewskmp.ui.theme.customThemeAttributes
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

@Composable
fun ItemImage(
    url: String,
    contentDescription: String = "unknown",
    modifier: Modifier = Modifier
) {
    KamelImage(
        resource = asyncPainterResource(Url(url)),
        contentDescription = "image of $contentDescription",
        modifier = modifier,
        contentScale = ContentScale.Crop,
        onLoading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .shimmerBackground()
            )
        },
        onFailure = { ErrorLoadingImageStateBox() }
    )
}

@Composable
private fun ErrorLoadingImageStateBox() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = "Error loading image",
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}

@Composable
fun ProductScreenError(message: String = "Error") {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}

@Composable
fun ProductScreenLoading() {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun GradientBackgroundContainer(
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.customThemeAttributes.secondaryBackground.copy(alpha = 0.3f),
                        MaterialTheme.customThemeAttributes.secondaryBackground.copy(alpha = 0.6f),
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun NetworkUnavailableElement(
    modifier: Modifier = Modifier,
    onActionButtonClick: () -> Unit = {},
) {
    GenericErrorScreen(
        modifier = modifier,
        icon = Icons.Rounded.NetworkCheck,
        title = stringResource(MR.strings.no_internet_connection),
        description = stringResource(MR.strings.please_check_your_internet_connection_and_try_again),
        actionButtonText = stringResource(MR.strings.try_again),
        onActionButtonClick = onActionButtonClick
    )
}

@Composable
fun UnknownErrorElement(
    modifier: Modifier = Modifier,
    onActionButtonClick: () -> Unit = {},
) {
    GenericErrorScreen(
        modifier = modifier,
        icon = Icons.Rounded.ErrorOutline,
        title = stringResource(MR.strings.something_went_wrong),
        description = stringResource(MR.strings.please_try_again_later),
        actionButtonText = stringResource(MR.strings.try_again),
        onActionButtonClick = onActionButtonClick
    )
}