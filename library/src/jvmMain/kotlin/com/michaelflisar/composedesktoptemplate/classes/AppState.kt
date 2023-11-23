package com.michaelflisar.composedesktoptemplate.classes

import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.michaelflisar.composedesktoptemplate.internal.InfoType
import com.michaelflisar.composedesktoptemplate.internal.LogLine
import com.michaelflisar.composedesktoptemplate.settings.Settings
import java.io.File

val LocalAppState = compositionLocalOf<AppState> { error("No value provided") }

@Composable
fun rememberAppState(
    settingsFile: File
): AppState {
    val settings = remember(settingsFile) { mutableStateOf(Settings.read(settingsFile)) }
    val state = remember { mutableStateOf<Status>(Status.None) }
    val logs = remember { mutableStateListOf<LogLine>() }
    val customStatusInfos = remember { mutableStateOf<List<StatusInfo>>(emptyList()) }
    val countLogs = remember { derivedStateOf { logs.size } }
    val countLogErrors = remember { derivedStateOf { logs.count { it.type == InfoType.Error } } }
    return AppState(settings, state, logs, customStatusInfos, countLogs, countLogErrors)
}

@Immutable
data class AppState internal constructor(
    val settings: MutableState<Settings>,
    val state: MutableState<Status>,
    internal val logs: SnapshotStateList<LogLine>,
    val customStatusInfos: MutableState<List<StatusInfo>>,
    val countLogs: State<Int>,
    val countLogErrors: State<Int>
) {
    fun setState(status: Status) {
        state.value = status
    }
}