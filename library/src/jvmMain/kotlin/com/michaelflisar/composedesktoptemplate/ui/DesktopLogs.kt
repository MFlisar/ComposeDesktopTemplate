package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.internal.LogLine
import com.michaelflisar.composedesktoptemplate.settings.Settings
import com.michaelflisar.composedesktoptemplate.settings.UISetting
import com.michaelflisar.composedesktoptemplate.ui.todo.MyCheckbox
import com.michaelflisar.composedesktoptemplate.ui.todo.MyScrollableLazyColumn
import com.michaelflisar.composedesktoptemplate.utils.Util

@Composable
fun DesktopLogs(
    autoScroll: UISetting.Bool,
    callerCellWidth: Dp = 128.dp
) {
    val appState = LocalAppState.current
    val autoScroll = autoScroll.getState(appState)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val logs = appState.logs
    LaunchedEffect(logs.size, autoScroll.value) {
        if (logs.size > 0 && autoScroll.value)
            listState.scrollToItem(logs.size - 1)
    }
    Column {
        MyScrollableLazyColumn(
            Modifier.fillMaxWidth().weight(1f),
            AppTheme.ITEM_SPACING_MINI,
            state = listState
        ) {
            if (logs.isEmpty()) {
                item(-1) {
                    Text(text = "No logs found yet!", style = MaterialTheme.typography.body2)
                }
            } else {
                logs.forEachIndexed { index, log ->
                    item {
                        LogLine(log, callerCellWidth)
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyCheckbox(title = "Auto Scroll", checked = autoScroll)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                enabled = appState.logs.isNotEmpty(),
                onClick = {
                    appState.logs.clear()
                    appState.showSnackbar(scope, "Logs cleared!")
                }
            ) {
                Icon(Icons.Default.Delete, null)
            }
            IconButton(
                enabled = appState.logs.isNotEmpty(),
                onClick = {
                    Util.setClipboard(logs.joinToString("\n") { it.getConsoleLog() })
                    appState.showSnackbar(scope, "Logs copied to clipboard!")
                }
            ) {
                Icon(Icons.Default.FileCopy, null)
            }
        }
    }
}

@Composable
private fun LogLine(
    log: LogLine,
    callerCellWidth: Dp
) {
    val color = log.type.getColor()
    val color2 = color.copy(alpha = 0.8f)
    var text = log.info
    log.exception?.let {
        text += "\n"
        text += it.message
        text += "\n"
        text += it.stackTraceToString()
    }

    val style = MaterialTheme.typography.body2

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
    ) {
        Text(
            modifier = Modifier.width(16.dp),
            text = log.type.shortcut,
            style = style,
            color = color,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.width(callerCellWidth),
            text = log.caller,
            style = style,
            color = color2,
            fontStyle = FontStyle.Italic
        )
        Text(modifier = Modifier.padding(start = 8.dp * log.inset), text = text, style = style, color = color)
    }
}