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
import org.pushingpixels.aurora.theming.IconFilterStrategy
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.*

@Composable
fun AuroraApplicationScope.DesktopApplication(
    title: String,
    state: AppState = rememberAppState(),
    visible: Boolean = true,
    icon: Painter? = null,
    iconFilterStrategy: IconFilterStrategy = IconFilterStrategy.Original,
    windowTitlePaneConfiguration: AuroraWindowTitlePaneConfiguration = AuroraWindowTitlePaneConfigurations.AuroraPlain(),
    resizable: Boolean = true,
    enabled: Boolean = true,
    focusable: Boolean = true,
    alwaysOnTop: Boolean = false,
    onPreviewKeyEvent: (androidx.compose.ui.input.key.KeyEvent) -> Boolean = { false },
    onKeyEvent: (androidx.compose.ui.input.key.KeyEvent) -> Boolean = { false },
    menuCommands: @Composable (() -> CommandGroup)? = null,
    onClosed: (suspend () -> Unit)? = null,
    colors: Colors = DesktopApp.Constants.COLORS,
    content: @Composable AuroraWindowScope.() -> Unit
) {
    MaterialTheme(
        colors = colors
    ) {
        CompositionLocalProvider(LocalAppState provides state) {
            val appState = LocalAppState.current
            val scope = rememberCoroutineScope()
            val windowState = rememberWindowState(scope, appState)
            AuroraWindow(
                skin = businessBlackSteelSkin(),
                windowTitlePaneConfiguration = windowTitlePaneConfiguration,
                state = windowState,
                onCloseRequest = {
                    scope.launch {
                        onClosed?.invoke()
                        appState.close.value = true
                    }
                },
                title = title,
                icon = icon,
                resizable = resizable,
                alwaysOnTop = alwaysOnTop,
                menuCommands = menuCommands?.invoke(),
                visible = visible,
                iconFilterStrategy = iconFilterStrategy,
                enabled = enabled,
                focusable = focusable,
                onPreviewKeyEvent = onPreviewKeyEvent,
                onKeyEvent = onKeyEvent
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
