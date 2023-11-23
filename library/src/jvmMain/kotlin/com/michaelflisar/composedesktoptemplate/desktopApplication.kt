package com.michaelflisar.composedesktoptemplate

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.rememberAppState
import com.michaelflisar.composedesktoptemplate.internal.rememberWindowState
import com.michaelflisar.composedesktoptemplate.ui.MainScreen
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.theming.businessBlackSteelSkin
import org.pushingpixels.aurora.window.*
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun DesktopApplication(
    settings: File? = null,
    content: @Composable AuroraApplicationScope.() -> Unit
) {
    val settings = settings ?: File(Paths.get("").absolutePathString(), "settings.dat")
    auroraApplication {
        val appState = rememberAppState(settings)
        CompositionLocalProvider(LocalAppState provides appState) {
            content()
        }
    }
}