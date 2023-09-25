package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

private const val TAG = "ExpandableText"

private const val ANNOTATED_STRING_TAG = "MORE"

@Composable
fun ExpandableText(
    text: String,
    showMoreText: String,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurface,
    collapsedMaxLine: Int = 6,
    showMoreStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.ExtraBold,
        textDecoration = TextDecoration.None,
        color = MaterialTheme.colorScheme.primary,
        background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
    ),
    onUnAnnotatedTextClick: () -> Unit = {}
) {

    var isExpanded by remember { mutableStateOf(false) }
    var clickable by remember { mutableStateOf(false) }
    var lastCharIndex by remember { mutableStateOf(0) }

    val textSpanStyle = style.toSpanStyle().copy(color = color)
    Box(
        modifier = Modifier.then(modifier)
    ) {
        val annotatedString = buildAnnotatedString {
            if (clickable) {
                if (isExpanded) {
                    withStyle(style = textSpanStyle) { append(text) }
                } else {
                    //TODO : fix this
                    // java.lang.StringIndexOutOfBoundsException: length=0; index=109
                    // at java.lang.String.substring(String.java:2060)
                    // at com.example.mor.nytnews.ui.common.ExpandableTextKt.ExpandableText-8r3B23s(ExpandableText.kt:54)

                    //temporary solution for the crash above
                    val adjustText = try {
                        text.substring(startIndex = 0, endIndex = lastCharIndex)
                            .dropLast(showMoreText.length)
                            .dropLastWhile { it.isWhitespace() || it == '.' }
                    } catch (e: Exception) {
                        println("ExpandableText: last char index is out of bounds. Text string length is: ${text.length}")
                    }
                    withStyle(style = textSpanStyle) { append(adjustText.toString()) }
                    pushStringAnnotation(tag = ANNOTATED_STRING_TAG, annotation = showMoreText)
                    withStyle(style = showMoreStyle) { append(showMoreText) }
                }
            } else {
                withStyle(style = textSpanStyle) { append(text) }
            }
        }
        ClickableText(
            modifier = textModifier
                .fillMaxWidth()
                .animateContentSize(),
            text = annotatedString,
            maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLine,
            onTextLayout = { textLayoutResult ->
                if (!isExpanded && textLayoutResult.didOverflowHeight) {
                    clickable = true
                    lastCharIndex = textLayoutResult.getLineEnd(collapsedMaxLine - 1, false)
                }
            },
            style = style,
            onClick = {
                val isMoreInRange =
                    annotatedString.getStringAnnotations(ANNOTATED_STRING_TAG, it, it).firstOrNull()

                if (isMoreInRange != null) {
                    isExpanded = !isExpanded
                } else {
                    onUnAnnotatedTextClick()
                }
            }
        )
    }
}