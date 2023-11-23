package com.michaelflisar.composedesktoptemplate.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.michaelflisar.composedesktoptemplate.DesktopApplication
import com.michaelflisar.composedesktoptemplate.DesktopWindow
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.Status
import com.michaelflisar.composedesktoptemplate.ui.*
import com.michaelflisar.composedesktoptemplate.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.component.model.CommandMenuContentModel
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun main() {
    val path = File(Paths.get("").absolutePathString(), "settings.dat")
    DesktopApplication(
        settings = path
    ) {
        // init
        val appState = LocalAppState.current
        val autoOpenLogView = Settings.AUTO_OPEN_LOG_PAGE_ON_NEW_LOG_ERROR.getState(appState)
        val expandedRightPane = Settings.EXPANDED_RIGHT_PANE.getState(appState)
        LaunchedEffect(autoOpenLogView.value, appState.countLogErrors.value) {
            if (autoOpenLogView.value) {
                if (!expandedRightPane.value)
                    expandedRightPane.value = true
            }
        }

        DesktopWindow(
            "Demo App",
            leftPanel = { ContentLeft(it) },
            rightPanel = { ContentRight(it) },
            centerPanel = { ContentCenter(it) },
            menuCommands = { buildMenu() }
            //icon = painterResource("logo.png"),
            //alwaysOnTop = alwaysOnTop.value,
            //onClosed = {
            //    val job = NamedPipe.stopAll(scope, null)
            //    job.join()
            //},
            //footer = {
            //    MainFooter(it, pipeState, filteredData, data, user)
            //}
        )
    }
}

@Composable
private fun ContentLeft(modifier: Modifier) {
    DesktopPaneSide(
        modifier,
        PaneSide.Left,
        "Settings",
        Settings.EXPANDED_LEFT_PANE
    ) {
        DesktopSettings(
            settings = Settings.ALL
        )
    }
}

@Composable
private fun ContentRight(modifier: Modifier) {
    DesktopPaneSide(
        modifier,
        PaneSide.Right,
        "Logs",
        Settings.EXPANDED_RIGHT_PANE
    ) {
        DesktopLogs()
    }
}

@Composable
private fun ContentCenter(modifier: Modifier) {
    val appState = LocalAppState.current
    val scope = rememberCoroutineScope()
    DesktopPaneCenter(modifier = modifier) {
        Column {
            Text(text = "Center")
            Button(
                onClick = {
                    L.d(appState, "Test Log")
                }
            ) {
                Text("Test Log")
            }
            Button(
                onClick = {
                    L.e(appState, "Test Error")
                }
            ) {
                Text("Test Error")
            }
            Button(
                enabled = appState.state.value == Status.None,
                onClick = {
                    if (appState.state.value == Status.None) {
                        scope.launch(Dispatchers.IO) {
                            L.d(appState, "Work wird gestartet...")
                            appState.state.value = Status.Running("Doing some work...")
                            delay(5000)
                            appState.state.value = Status.None
                            L.d(appState, "Work erfolgreich erledigt!", true)
                        }
                    } else {
                        L.e(appState, "Work läuft bereits!!")
                    }
                }
            ) {
                Text("Test Work for 5 seconds...")
            }
        }
    }
}

@Composable
private fun buildMenu(): CommandGroup {
    val appState = LocalAppState.current
    val commandGroup = CommandGroup(
        commands = listOf(
            Command(
                text = "App",
                secondaryContentModel = CommandMenuContentModel(
                    CommandGroup(
                        commands = listOf(
                            Command(
                                text = "New",
                                action = {
                                    L.d(appState, "Menu => New")
                                }),
                            Command(
                                text = "Open",
                                action = {
                                    L.d(appState, "Menu => Open")
                                }),
                            Command(
                                text = "Save",
                                action = {
                                    L.d(appState, "Menu => Save")
                                })
                        )
                    )
                )
            ),
            Command(
                text = "Edit",
                action = {
                    L.d(appState, "Menu => Edit")
                })
        )
    )
    return commandGroup
}
