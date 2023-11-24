package com.michaelflisar.composedesktoptemplate

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.ui.internal.StatusBar
import com.michaelflisar.composedesktoptemplate.ui.todo.MyVerticalDivider
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
    maxWidthLeftInPercentages: Float = 1f/5f,
    maxWidthRightInPercentages: Float = 1f/5f
) {
    val appState = LocalAppState.current
    MaterialTheme(
        colors = lightColors(
            primary = Color(0xff1976d2),
            primaryVariant = Color(0xffc1dcf7),
            secondary = Color(0xff00c853),
            secondaryVariant = Color(0xff91f5ba)
        )
    ) {
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
                    Row(modifier = Modifier.fillMaxHeight()) {
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
                }
            }
            StatusBar(footer)
        }
    }
}

