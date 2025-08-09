package com.mls.kmp.mor.nytnewskmp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mls.kmp.mor.nytnewskmp.ui.articles.LoadingShimmerArticlesList
import com.mls.kmp.mor.nytnewskmp.ui.theme.MyAppTheme

@Preview
@Composable
fun AppPreviewLightTheme() {
    MyAppTheme {
        MyApp(false)
    }
}

@Preview
@Composable
fun AppPreviewDarkTheme() {
    MyAppTheme {
        MyApp(true)
    }
}

@Preview
@Composable
fun ShimmerArticleListPreview() {
    MyAppTheme {
        LoadingShimmerArticlesList()
    }
}

@Preview
@Composable
fun Prev() {
    MyAppTheme {
        LoadingShimmerArticlesList()
    }
}