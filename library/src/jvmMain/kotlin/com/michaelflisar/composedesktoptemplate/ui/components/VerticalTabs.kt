package com.michaelflisar.composedesktoptemplate.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val LocalVerticalTabStyle = compositionLocalOf { VerticalTabStyle() }

class VerticalTabStyle(
    val marker: Boolean = true,
    val markerStyle: Style = Style.Stripe()
) {
    enum class Side {
        Left,
        Right
    }

    sealed class Style {
        data object None : Style()
        class Stripe(val side: Side = Side.Left, val width: Dp = 16.dp) : Style() {

        }

        class Highlight(val side: Side = Side.Left, val backgroundColor: Color, val contentColor: Color) : Style()
    }
}

@Composable
fun VerticalTabs(
    modifier: Modifier = Modifier,
    verticalTabStyle: VerticalTabStyle = VerticalTabStyle(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colors.background,
        LocalVerticalTabStyle provides verticalTabStyle
    ) {
        Column(
            modifier = modifier.background(MaterialTheme.colors.onBackground)
        ) {
            content()
        }
    }
}

@Composable
fun VerticalTabItem(
    label: String,
    tabIndex: Int,
    selectedTab: MutableState<Int>
) {
    val style = LocalVerticalTabStyle.current
    val selected = remember(tabIndex, selectedTab.value) {
        derivedStateOf {
            selectedTab.value == tabIndex
        }
    }
    Row(
        modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()
    ) {
        Marker(
            style,
            VerticalTabStyle.Side.Left,
            selected
        )
        TabButton(style, selected, label, tabIndex, selectedTab)
        Marker(
            style,
            VerticalTabStyle.Side.Right,
            selected
        )
    }
}

@Composable
private fun Marker(
    style: VerticalTabStyle,
    side: VerticalTabStyle.Side,
    selected: State<Boolean>
) {
    val markerStyle = style.markerStyle
    if (markerStyle !is VerticalTabStyle.Style.Stripe)
        return
    if (markerStyle.side != side)
        return
    val indicatorWidth by animateDpAsState(if (selected.value) 8.dp else 0.dp)
    Box(modifier = Modifier.width(indicatorWidth).fillMaxHeight().background(MaterialTheme.colors.primary))
}

@Composable
private fun RowScope.TabButton(
    style: VerticalTabStyle,
    selected: State<Boolean>,
    label: String,
    tabIndex: Int,
    selectedTab: MutableState<Int>
) {
    val markerStyle = style.markerStyle
    when (markerStyle) {
        VerticalTabStyle.Style.None,
        is VerticalTabStyle.Style.Stripe -> {
            OutlinedButton(
                modifier = Modifier.fillMaxHeight().weight(1f),
                onClick = {
                    selectedTab.value = tabIndex
                },
                shape = RectangleShape
            ) {
                Text(
                    text = label,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
            }
        }

        is VerticalTabStyle.Style.Highlight -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .width(IntrinsicSize.Min)
                    .background(MaterialTheme.colors.surface)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = selected.value,
                    modifier = Modifier
                        .align(if (markerStyle.side == VerticalTabStyle.Side.Left) Alignment.CenterStart else Alignment.CenterEnd),
                    enter = expandHorizontally(
                        //expandFrom = if (markerStyle.side == VerticalTabStyle.Side.Left) Alignment.Start else Alignment.End
                    ),
                    exit = shrinkHorizontally(
                        //shrinkTowards = if (markerStyle.side == VerticalTabStyle.Side.Left) Alignment.Start else Alignment.End
                    )
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(markerStyle.backgroundColor)
                    )
                }
                val contentColor by animateColorAsState(
                    if (selected.value) {
                        markerStyle.takeIf { selected.value }?.let { it.contentColor } ?: MaterialTheme.colors.primary
                    } else MaterialTheme.colors.primary
                )
                OutlinedButton(
                    modifier = Modifier.fillMaxHeight(),
                    onClick = {
                        selectedTab.value = tabIndex
                    },
                    shape = RectangleShape,
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = contentColor
                    )
                ) {
                    Text(
                        text = label,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}