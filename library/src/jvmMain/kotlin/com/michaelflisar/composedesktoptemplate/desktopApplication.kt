package com.michaelflisar.composedesktoptemplate

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.AppState
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.rememberAppState
import com.michaelflisar.composedesktoptemplate.internal.rememberWindowState
import com.michaelflisar.composedesktoptemplate.ui.MainScreen
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.AuroraWindow
import org.pushingpixels.aurora.window.AuroraWindowTitlePaneConfigurations
import org.pushingpixels.aurora.window.auroraApplication
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun desktopApplication(
    appName: String,
    centerPanel: @Composable (modifier: Modifier) -> Unit,
    settings: File? = null,
    leftPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    rightPanel: (@Composable (modifier: Modifier) -> Unit)? = null,
    menuCommands: @Composable (() ->CommandGroup)? = null,
    init: @Composable (() -> Unit)? = null
) {
    val settings = settings ?: File(Paths.get("").absolutePathString(), "settings.dat")
    auroraApplication {
        val appState = rememberAppState(settings)
        CompositionLocalProvider(LocalAppState provides appState) {
            val appState = LocalAppState.current
            val scope = rememberCoroutineScope()
            val windowState = rememberWindowState(scope, appState)
            val close = remember { mutableStateOf(false) }
            init?.invoke()
            AuroraWindow(
                skin = businessBlackSteelSkin(),
                windowTitlePaneConfiguration = AuroraWindowTitlePaneConfigurations.AuroraPlain(),
                state = windowState,
                onCloseRequest = {
                    close.value = true
                },
                title = appName,
                menuCommands = menuCommands?.invoke()
            ) {
                MainScreen(
                    this@auroraApplication,
                    close.value,
                    leftPanel,
                    rightPanel,
                    centerPanel
                )
            }
        }

    }
}