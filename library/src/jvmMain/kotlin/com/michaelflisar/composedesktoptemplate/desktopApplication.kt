package com.michaelflisar.composedesktoptemplate

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.michaelflisar.composedesktoptemplate.classes.AppState
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.rememberAppState
import com.michaelflisar.composedesktoptemplate.internal.rememberWindowState
import kotlinx.coroutines.launch
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.AuroraApplicationScope
import org.pushingpixels.aurora.window.AuroraWindow
import org.pushingpixels.aurora.window.AuroraWindowScope
import org.pushingpixels.aurora.window.AuroraWindowTitlePaneConfigurations

@Composable
fun AuroraApplicationScope.DesktopApplication(
    appName: String,
    appState: AppState = rememberAppState(),
    icon: Painter? = null,
    alwaysOnTop: Boolean = false,
    resizeable: Boolean = true,
    menuCommands: @Composable (() -> CommandGroup)? = null,
    onClosed: (suspend () -> Unit)? = null,
    colors: Colors = DesktopApp.Constants.COLORS,
    content: @Composable AuroraWindowScope.() -> Unit
) {
    MaterialTheme(
        colors = colors
    ) {
        CompositionLocalProvider(LocalAppState provides appState) {
            val appState = LocalAppState.current
            val scope = rememberCoroutineScope()
            val windowState = rememberWindowState(scope, appState)
            AuroraWindow(
                skin = businessBlackSteelSkin(),
                windowTitlePaneConfiguration = AuroraWindowTitlePaneConfigurations.AuroraPlain(),
                state = windowState,
                onCloseRequest = {
                    scope.launch {
                        onClosed?.invoke()
                        appState.close.value = true
                    }
                },
                title = appName,
                icon = icon,
                resizable = resizeable,
                alwaysOnTop = alwaysOnTop,
                menuCommands = menuCommands?.invoke()
            ) {
                content()
            }

            // Close Action
            if (appState.close.value) {
                appState.settings.value.write()
                exitApplication()
            }
        }
    }
}
