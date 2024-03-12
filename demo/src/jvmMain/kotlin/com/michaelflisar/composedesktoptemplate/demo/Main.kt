package com.michaelflisar.composedesktoptemplate.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import com.michaelflisar.composedesktoptemplate.DesktopApplication
import com.michaelflisar.composedesktoptemplate.DesktopMainScreen
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.Status
import com.michaelflisar.composedesktoptemplate.classes.rememberAppState
import com.michaelflisar.composedesktoptemplate.ui.*
import com.michaelflisar.composedesktoptemplate.ui.components.VerticalTabIconItem
import com.michaelflisar.composedesktoptemplate.ui.components.VerticalTabItem
import com.michaelflisar.composedesktoptemplate.ui.components.VerticalTabStyle
import com.michaelflisar.composedesktoptemplate.ui.components.VerticalTabs
import com.michaelflisar.composedesktoptemplate.ui.todo.MyDropdown
import com.michaelflisar.composedesktoptemplate.utils.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.pushingpixels.aurora.component.model.Command
import org.pushingpixels.aurora.component.model.CommandGroup
import org.pushingpixels.aurora.component.model.CommandMenuContentModel
import org.pushingpixels.aurora.window.auroraApplication

fun main() {
    auroraApplication {
        val appState = rememberAppState()
        DesktopApplication(
            title = "Demo App",
            state = appState,
            menuCommands = { buildMenu() },
            //icon = painterResource("logo.png"),
            //alwaysOnTop = alwaysOnTop.value,
            //onClosed = {
            //    val job = NamedPipe.stopAll(scope, null)
            //    job.join()
            //}
        ) {
            // init
            val autoOpenLogView = Settings.AUTO_OPEN_LOG_PAGE_ON_NEW_LOG_ERROR.getState(appState)
            val expandedRightPane = Settings.EXPANDED_RIGHT_PANE.getState(appState)
            LaunchedEffect(autoOpenLogView.value, appState.countLogErrors.value) {
                if (autoOpenLogView.value) {
                    if (!expandedRightPane.value)
                        expandedRightPane.value = true
                }
            }

            // UI
            DesktopMainScreen(
                leftPanel = { ContentLeft(it) },
                rightPanel = { ContentRight(it) },
                centerPanel = { ContentCenter(it) },
                //footer = {
                //    MainFooter(it, pipeState, filteredData, data, user)
                //}
            )
        }
    }

}

@Composable
private fun ContentLeft(modifier: Modifier) {
    DesktopPaneSide(
        modifier,
        PaneSide.Left,
        "Settings",
        Settings.EXPANDED_LEFT_PANE,
        //showLabelWhenCollapsed = false
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
        DesktopLogs(autoScroll = Settings.AUTO_SCROLL_LOGS)
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
            val selectedTab = remember { mutableStateOf(0) }
            VerticalTabs(
                verticalTabStyle = VerticalTabStyle.Highlight(
                    side = VerticalTabStyle.Side.Left,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                VerticalTabItem("Tab 1", 0, selectedTab)
                VerticalTabItem("Tab 2", 1, selectedTab)
                VerticalTabItem("Tab 3", 2, selectedTab)
            }
            val selectedTab2 = remember { mutableStateOf(0) }
            VerticalTabs(
                verticalTabStyle = VerticalTabStyle.Highlight(
                    side = VerticalTabStyle.Side.Left,
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary
                )
            ) {
                VerticalTabIconItem(rememberVectorPainter(Icons.Default.Home), 0, selectedTab2)
                VerticalTabIconItem(rememberVectorPainter(Icons.Default.Settings), 1, selectedTab2)
                VerticalTabIconItem(rememberVectorPainter(Icons.Default.Computer), 2, selectedTab2)
            }

            val selectedIndex = remember { mutableStateOf(0) }
            MyDropdown(
                title = "List",
                items = (1..5).map { "Item $it" },
                selected = selectedIndex,
                filter = MyDropdown.Filter("Search") { filter, item ->
                    filter.isEmpty() || item.contains(filter, true)
                }
            )
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
                }
            )
        )
    )
    return commandGroup
}
