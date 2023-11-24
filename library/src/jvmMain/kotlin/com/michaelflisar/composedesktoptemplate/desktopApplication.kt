package com.michaelflisar.composedesktoptemplate

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import com.michaelflisar.composedesktoptemplate.classes.AppState
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.rememberAppState
import com.michaelflisar.composedesktoptemplate.internal.rememberWindowState
import kotlinx.coroutines.launch
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.*
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

@Composable
fun AuroraApplicationScope.DesktopApplication(
    appName: String,
    appState: AppState = rememberAppState(),
    icon: Painter? = null,
    alwaysOnTop: Boolean = false,
    resizeable: Boolean = true,
    menuCommands: @Composable (() -> CommandGroup)? = null,
    onClosed: (suspend () -> Unit)? = null,
    content: @Composable AuroraWindowScope.() -> Unit
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
