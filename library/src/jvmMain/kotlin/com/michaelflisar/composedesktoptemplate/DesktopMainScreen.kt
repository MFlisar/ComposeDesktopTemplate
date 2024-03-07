package com.michaelflisar.composedesktoptemplate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.ui.internal.StatusBar
import org.pushingpixels.aurora.theming.auroraBackground

// ---------------------
// Main Layout
// ---------------------

@Composable
@Preview
fun DesktopMainScreen(
    leftPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    rightPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    footer: (@Composable (modifier: Modifier) -> Unit)? = null,
    centerPanel: @Composable (modifier: Modifier) -> Unit,
    showInfosInFooter: Boolean = true,
    showErrorsInFooter: Boolean = true,
    maxWidthLeftInPercentages: Float = 1f / 5f,
    maxWidthRightInPercentages: Float = 1f / 5f
) {
    val appState = LocalAppState.current
    Column {
        Scaffold(
            modifier = Modifier.weight(1f),
            scaffoldState = appState.scaffoldState
        ) {
            BoxWithConstraints(
                modifier = Modifier.auroraBackground()
            ) {
                val maxWidthLeft = maxWidth * maxWidthLeftInPercentages
                val maxWidthRight = maxWidth * maxWidthRightInPercentages
                Row(modifier = Modifier.fillMaxSize()) {
                    if (leftPanel != null) {
                        leftPanel(Modifier.sizeIn(maxWidth = maxWidthLeft))
                    }
                    centerPanel(Modifier.weight(1f).fillMaxHeight())
                    if (rightPanel != null) {
                        rightPanel(Modifier.sizeIn(maxWidth = maxWidthRight).fillMaxHeight())
                    }
                }
            }
        }
        StatusBar(footer, showInfosInFooter, showErrorsInFooter)
    }
}

