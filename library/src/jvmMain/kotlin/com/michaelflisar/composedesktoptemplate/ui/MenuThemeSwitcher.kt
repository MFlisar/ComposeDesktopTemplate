package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.runtime.*
import com.michaelflisar.composedesktoptemplate.utils.L
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.theming.AuroraSkin
import org.pushingpixels.aurora.theming.AuroraSkinDefinition
import org.pushingpixels.aurora.theming.PopupPlacementStrategy
import org.pushingpixels.aurora.theming.getAuroraSkins

@Composable
fun rememberTheme(skinName: String) : State<AuroraSkinDefinition> {
    return remember(skinName) {
        derivedStateOf {
            getAuroraSkins().find { it.first == skinName }!!.second()
        }
    }
}
object MenuThemeSwitcher {

    fun getCommands(onSkinChange: (AuroraSkinDefinition) -> Unit) : List<Command> {
        val auroraSkins = getAuroraSkins()
        return auroraSkins.map {
            Command(
                text = it.first,
                action = {
                   onSkinChange(it.second())
                }
            )
        }
    }
}