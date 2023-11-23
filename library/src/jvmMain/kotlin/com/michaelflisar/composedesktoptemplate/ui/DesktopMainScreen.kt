package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.ui.internal.StatusBar
import com.michaelflisar.composedesktoptemplate.ui.todo.MyVerticalDivider
import org.pushingpixels.aurora.window.AuroraApplicationScope

// ---------------------
// Main Layout
// ---------------------

@Composable
@Preview
internal fun MainScreen(
    applicationScope: AuroraApplicationScope,
    close: Boolean,
    leftPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    rightPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    footer: (@Composable (modifier: Modifier) -> Unit)? = null,
    centerPanel: @Composable (modifier: Modifier) -> Unit
) {
    MaterialTheme {
        BoxWithConstraints {
            val maxWidthLeft = maxWidth / 5
            val maxWidthRight = maxWidth / 5
            Column {
                Row(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    if (leftPanel != null) {
                        leftPanel(Modifier.sizeIn(maxWidth = maxWidthLeft))
                        MyVerticalDivider()
                    }
                    centerPanel(Modifier.weight(1f).fillMaxHeight())
                    if (rightPanel != null) {
                        MyVerticalDivider()
                        rightPanel(Modifier.sizeIn(maxWidth = maxWidthRight).fillMaxHeight())
                    }
                }
                StatusBar(footer)
            }
        }

        // Close Action
        if (close) {
            val appState = LocalAppState.current
            appState.settings.value.write()
            applicationScope.exitApplication()
        }
    }
}

