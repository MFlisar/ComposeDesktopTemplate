package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.settings.UISetting
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.ui.todo.MyVerticalDivider
import org.pushingpixels.aurora.theming.LocalSkinColors
import org.pushingpixels.aurora.theming.LocalTextColor
import org.pushingpixels.aurora.theming.colorscheme.AuroraColorScheme

enum class PaneSide {
    Left, Right
}

@Composable
fun DesktopPaneSide(
    modifier: Modifier,
    side: PaneSide,
    label: String,
    expanded: UISetting.Bool,
    divider: Boolean = true,
    showLabelWhenCollapsed: Boolean = true,
    collapsible: Boolean = true,
    content: (@Composable ColumnScope.() -> Unit)
) {
    val appState = LocalAppState.current
    Row(modifier = modifier) {
        if (side == PaneSide.Right && divider) {
            MyVerticalDivider()
        }
        Column(
            modifier = Modifier.fillMaxHeight() .weight(1f, fill = false),
            horizontalAlignment = if (side == PaneSide.Left) Alignment.Start else Alignment.End
        ) {
            // Header (Icon + Title)
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (side == PaneSide.Left) {
                    if (collapsible)
                        ExpandIcon(expanded)
                    Title(label, expanded)
                    //Spacer(Modifier.width(AppTheme.CONTENT_PADDING_SMALL))
                } else {
                    //Spacer(Modifier.width(AppTheme.CONTENT_PADDING_SMALL))
                    Title(label, expanded)
                    if (collapsible)
                        ExpandIcon(expanded)
                }
            }

            Box(
                modifier = Modifier.fillMaxHeight().align(Alignment.CenterHorizontally)
            ) {

                // Content
                androidx.compose.animation.AnimatedVisibility(
                    modifier = Modifier.align(if (side == PaneSide.Left) Alignment.CenterStart else Alignment.CenterEnd),
                    visible = expanded.getState(appState).value,
                    enter = fadeIn() + expandHorizontally(
                        expandFrom = if (side == PaneSide.Left) Alignment.End else Alignment.Start
                    ),
                    exit = fadeOut() + shrinkHorizontally(
                        shrinkTowards = if (side == PaneSide.Left) Alignment.End else Alignment.Start
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(AppTheme.CONTENT_PADDING_SMALL)
                        //.verticalScroll(rememberScrollState())
                        ,
                        verticalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
                    ) {
                        content()
                    }
                }

                // Text
                if (showLabelWhenCollapsed) {
                    androidx.compose.animation.AnimatedVisibility(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .zIndex(1f),
                        visible = !expanded.getState(appState).value,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        VerticalTitle(side, label) {
                            expanded.updateValue(appState, !expanded.getState(appState).value)
                        }
                    }
                }
            }
        }
        if (side == PaneSide.Left && divider) {
            MyVerticalDivider()
        }
    }
}

@Composable
private fun ExpandIcon(
    expanded: UISetting.Bool,
    modifier: Modifier = Modifier
) {
    val appState = LocalAppState.current
    IconButton(
        modifier = modifier,
        onClick = {
            expanded.updateValue(appState, !expanded.getState(appState).value)
        }) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = null
        )
    }
}

@Composable
private fun Title(
    label: String,
    expanded: UISetting.Bool,
    modifier: Modifier = Modifier
) {
    val appState = LocalAppState.current
    AnimatedVisibility(
        visible = expanded.getState(appState).value,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally(),
    ) {
        Text(
            modifier = modifier.padding(AppTheme.ITEM_SPACING),
            text = label,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun VerticalTitle(
    side: PaneSide,
    label: String,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier

            .clip(MaterialTheme.shapes.small)
            .clickable { onClick() }
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .vertical()
                .rotate(if (side == PaneSide.Left) -90f else 90f),
            text = label,
            style = MaterialTheme.typography.h6,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

private fun Modifier.vertical() =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.height, placeable.width) {
            placeable.place(
                x = -(placeable.width / 2 - placeable.height / 2),
                y = -(placeable.height / 2 - placeable.width / 2)
            )
        }
    }