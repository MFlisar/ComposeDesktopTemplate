package com.michaelflisar.composedesktoptemplate.internal

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

internal data class LogLine(
    val caller: String,
    val info: String,
    val exception: Exception? = null,
    val inset: Int = 0,
    val type: InfoType = InfoType.Debug,
    val fontWeight: FontWeight = FontWeight.Normal
) {
    fun getConsoleLog(): String {
        var log = "[${caller}] ${info}"
        if (exception != null) {
            log += " | EXCEPTION: ${exception.message}"
        }
        return log
    }
}

internal enum class InfoType(
    val shortcut: String
) {
    Debug("D"),
    Error("E"),
    DebugSuccess("D");

    @Composable
    fun getColor() = when (this) {
        Debug -> Color.Unspecified
        Error -> MaterialTheme.colors.error
        DebugSuccess -> AppTheme.COLOR_GREEN
    }
}