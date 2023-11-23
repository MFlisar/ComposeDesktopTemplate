package com.michaelflisar.composedesktoptemplate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.internal.rememberWindowState
import com.michaelflisar.composedesktoptemplate.ui.MainScreen
import kotlinx.coroutines.launch
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraWindow
import org.pushingpixels.aurora.window.AuroraWindowTitlePaneConfigurations

@Composable
fun AuroraApplicationScope.DesktopWindow(
    appName: String,
    icon: Painter? = null,
    alwaysOnTop: Boolean = false,
    centerPanel: @Composable (modifier: Modifier) -> Unit,
    leftPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    rightPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    maxWidthLeftInPercentages: Float = 1f/5f,
    maxWidthRightInPercentages: Float = 1f/5f,
    footer: (@Composable (modifier: Modifier) -> Unit)? = null,
    menuCommands: @Composable (() -> CommandGroup)? = null,
    onClosed: (suspend () -> Unit)? = null
) {
    val appState = LocalAppState.current
    val scope = rememberCoroutineScope()
    val windowState = rememberWindowState(scope, appState)
    val close = remember { mutableStateOf(false) }
    AuroraWindow(
        skin = businessBlackSteelSkin(),
        windowTitlePaneConfiguration = AuroraWindowTitlePaneConfigurations.AuroraPlain(),
        state = windowState,
        onCloseRequest = {
            scope.launch {
                onClosed?.invoke()
                close.value = true
            }
        },
        title = appName,
        icon = icon,
        alwaysOnTop = alwaysOnTop,
        menuCommands = menuCommands?.invoke()
    ) {
        MainScreen(
            this@DesktopWindow,
            close.value,
            leftPanel,
            rightPanel,
            footer,
            centerPanel,
            maxWidthLeftInPercentages,
            maxWidthRightInPercentages
        )
    }
}