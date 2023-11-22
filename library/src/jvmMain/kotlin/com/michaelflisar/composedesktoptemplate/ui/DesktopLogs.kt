package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.internal.LogLine
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.ui.todo.MyScrollableLazyColumn

@Composable
fun DesktopLogs() {
    val appState = LocalAppState.current
    val logs = appState.logs
    MyScrollableLazyColumn(
        Modifier.fillMaxWidth(),
        AppTheme.ITEM_SPACING_MINI
    ) {
        if (logs.isEmpty()) {
            item(-1) {
                Text(text = "No logs found yet!", style = MaterialTheme.typography.body2)
            }

        } else {
            logs.forEachIndexed { index, log ->
                item {
                    LogLine(log)
                }
            }
        }
    }
}

@Composable
private fun LogLine(log: LogLine) {
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
            modifier = Modifier.width(96.dp),
            text = log.caller,
            style = style,
            color = color2,
            fontStyle = FontStyle.Italic
        )
        Text(modifier = Modifier.padding(start = 8.dp * log.inset), text = text, style = style, color = color)
    }
}