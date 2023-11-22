package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.settings.UISetting
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

enum class PaneSide {
    Left, Right
}

@Composable
fun DesktopPaneSide(
    modifier: Modifier,
    side: PaneSide,
    label: String,
    expanded: UISetting.Bool,
    content: (@Composable ColumnScope.() -> Unit)
) {
    val appState = LocalAppState.current
    Column(
        modifier = modifier,
        horizontalAlignment = if (side == PaneSide.Left) Alignment.Start else Alignment.End
    ) {
        // Header (Icon + Title)
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (side == PaneSide.Left) {
                Icon(expanded)
                Title(label, expanded)
                Spacer(Modifier.width(AppTheme.CONTENT_PADDING_SMALL))
            } else {
                Spacer(Modifier.width(AppTheme.CONTENT_PADDING_SMALL))
                Title(label, expanded)
                Icon(expanded)
            }
        }
        // Content
        AnimatedVisibility(
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
                    .weight(1f)
                    .padding(AppTheme.CONTENT_PADDING_SMALL)
                    //.verticalScroll(rememberScrollState())
                ,
                verticalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun Icon(
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