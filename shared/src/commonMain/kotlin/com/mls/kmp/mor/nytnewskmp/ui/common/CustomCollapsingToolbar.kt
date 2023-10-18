@file:OptIn(ExperimentalMaterial3Api::class)

package com.mls.kmp.mor.nytnewskmp.ui.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationState
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.animateTo
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

private const val TAG = "CustomCollapsingToolbar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCollapsingToolbarContainer(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    initialHeight: Dp,
    alphaAnimation: Boolean = false,
    content: @Composable () -> Unit
) {
    // Set up support for resizing the top app bar when vertically dragging the bar itself.
    val appBarDragModifier = if (!scrollBehavior.isPinned) {
        Modifier.draggable(
            orientation = Orientation.Vertical,
            state = rememberDraggableState { delta ->
                scrollBehavior.state.heightOffset = scrollBehavior.state.heightOffset + delta
            },
            onDragStopped = { velocity ->
                settleAppBar(
                    scrollBehavior.state,
                    velocity,
                    scrollBehavior.flingAnimationSpec,
                    scrollBehavior.snapAnimationSpec
                )
            }
        )
    } else {
        Modifier
    }

    var toolbarFirstMeasuredHeightPx by remember { mutableStateOf(0f) }
    LocalDensity.current.run {
        SideEffect {
            toolbarFirstMeasuredHeightPx = initialHeight.toPx()
        }
    }

    LaunchedEffect(key1 = toolbarFirstMeasuredHeightPx) {
        scrollBehavior.state.heightOffsetLimit = -toolbarFirstMeasuredHeightPx
    }

    var contentHeightDp by remember {
        mutableStateOf(0.dp)
    }

    LocalDensity.current.run {
        LaunchedEffect(key1 = scrollBehavior.state.heightOffset) {
            contentHeightDp = toolbarFirstMeasuredHeightPx
                .plus(scrollBehavior.state.heightOffset)
                .absoluteValue
                .roundToInt()
                .toDp()
        }
    }

    Box(
        modifier = modifier.animateContentSize()
            .then(appBarDragModifier)
            .height(contentHeightDp)
            .then(
                if (alphaAnimation) Modifier.graphicsLayer(alpha = 1 - scrollBehavior.state.collapsedFraction)
                else Modifier
            )
    ) {
        content()
    }
}

/**
 * Settles the app bar by flinging, in case the given velocity is greater than zero, and snapping
 * after the fling settles.
 */
/**
 * Settles the app bar by flinging, in case the given velocity is greater than zero, and snapping
 * after the fling settles.
 */
@OptIn(ExperimentalMaterial3Api::class)
private suspend fun settleAppBar(
    state: TopAppBarState,
    velocity: Float,
    flingAnimationSpec: DecayAnimationSpec<Float>?,
    snapAnimationSpec: AnimationSpec<Float>?
): Velocity {
    // Check if the app bar is completely collapsed/expanded. If so, no need to settle the app bar,
    // and just return Zero Velocity.
    // Note that we don't check for 0f due to float precision with the collapsedFraction
    // calculation.
    if (state.collapsedFraction < 0.01f || state.collapsedFraction == 1f) {
        return Velocity.Zero
    }
    var remainingVelocity = velocity
    // In case there is an initial velocity that was left after a previous user fling, animate to
    // continue the motion to expand or collapse the app bar.
    if (flingAnimationSpec != null && abs(velocity) > 1f) {
        var lastValue = 0f
        AnimationState(
            initialValue = 0f,
            initialVelocity = velocity,
        )
            .animateDecay(flingAnimationSpec) {
                val delta = value - lastValue
                val initialHeightOffset = state.heightOffset
                state.heightOffset = initialHeightOffset + delta
                val consumed = abs(initialHeightOffset - state.heightOffset)
                lastValue = value
                remainingVelocity = this.velocity
                // avoid rounding errors and stop if anything is unconsumed
                if (abs(delta - consumed) > 0.5f) this.cancelAnimation()
            }
    }
    // Snap if animation specs were provided.
    if (snapAnimationSpec != null) {
        if (state.heightOffset < 0 &&
            state.heightOffset > state.heightOffsetLimit
        ) {
            AnimationState(initialValue = state.heightOffset).animateTo(
                if (state.collapsedFraction < 0.5f) {
                    0f
                } else {
                    state.heightOffsetLimit
                },
                animationSpec = snapAnimationSpec
            ) { state.heightOffset = value }
        }
    }

    return Velocity(0f, remainingVelocity)
}

private fun TopAppBarScrollBehavior.printState(tag: String) {
    state.run {
        println(
            "Container: heightOffsetLimit:$heightOffsetLimit | heightOffset:$heightOffset | collapsedFraction:$collapsedFraction| contentOffset:$contentOffset | overlappedFraction:$overlappedFraction"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarState.collapseAppBar() {
    this.heightOffset = -(heightOffsetLimit.absoluteValue)
}

@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBarState.expandAppBar() {
    this.heightOffset += heightOffsetLimit.absoluteValue
}
