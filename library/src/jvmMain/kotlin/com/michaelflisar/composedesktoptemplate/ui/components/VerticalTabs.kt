package com.michaelflisar.composedesktoptemplate.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class VerticalTabMarkerSide {
    Left, Right
}

@Composable
fun VerticalTabs(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.background) {
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
    selectedTab: MutableState<Int>,
    marker: VerticalTabMarkerSide = VerticalTabMarkerSide.Left,
) {
    val selected = remember(tabIndex, selectedTab.value) {
        derivedStateOf {
            selectedTab.value == tabIndex
        }
    }
    Row(
        modifier = Modifier.height(IntrinsicSize.Min).fillMaxWidth()
    ) {
        Marker(marker == VerticalTabMarkerSide.Left, selected)
        OutlinedButton(
            modifier = Modifier.fillMaxHeight().weight(1f),
            onClick = {
                selectedTab.value = tabIndex
            },
            shape = RectangleShape,
        ) {
            Text(
                text = label,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
        Marker(marker == VerticalTabMarkerSide.Right, selected)
    }
}

@Composable
private fun Marker(
    enabled: Boolean,
    selected: State<Boolean>
) {
    if (!enabled)
        return
    val indicatorWidth by animateDpAsState(if (selected.value) 8.dp else 0.dp)
    Box(modifier = Modifier.width(indicatorWidth).fillMaxHeight().background(MaterialTheme.colors.primary))
}