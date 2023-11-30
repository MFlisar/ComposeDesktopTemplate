package com.michaelflisar.composedesktoptemplate.classes

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.michaelflisar.composedesktoptemplate.internal.InfoType
import com.michaelflisar.composedesktoptemplate.internal.LogLine
import com.michaelflisar.composedesktoptemplate.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.skiko.MainUIDispatcher
import java.io.File
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

val LocalAppState = compositionLocalOf<AppState> { error("No value provided") }

@Composable
fun rememberAppState(
    settingsFile: File = File(Paths.get("").absolutePathString(), "settings.dat")
): AppState {
    val scope = rememberCoroutineScope()
    val settings = remember(settingsFile) { mutableStateOf(Settings.read(settingsFile)) }
    val state = remember { mutableStateOf<Status>(Status.None) }
    val logs = remember { mutableStateListOf<LogLine>() }
    val customStatusInfos = remember { mutableStateOf<List<StatusInfo>>(emptyList()) }
    val countLogs = remember { derivedStateOf { logs.size } }
    val countLogErrors = remember { derivedStateOf { logs.count { it.type == InfoType.Error } } }
    val scaffoldState = rememberScaffoldState()
    val close = remember { mutableStateOf(false) }
    return AppState(scope, settings, state, logs, customStatusInfos, countLogs, countLogErrors, scaffoldState, close)
}

@Immutable
data class AppState internal constructor(
    private val scope: CoroutineScope,
    val settings: MutableState<Settings>,
    val state: MutableState<Status>,
    internal val logs: SnapshotStateList<LogLine>,
    val customStatusInfos: MutableState<List<StatusInfo>>,
    val countLogs: State<Int>,
    val countLogErrors: State<Int>,
    val scaffoldState: ScaffoldState,
    val close: MutableState<Boolean>
) {
    fun setState(status: Status) {
        scope.launch(MainUIDispatcher) {
            state.value = status
        }
    }

    fun showSnackbar(
        scope: CoroutineScope,
        info: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        scope.launch(MainUIDispatcher) {
            scaffoldState.snackbarHostState.showSnackbar(info, actionLabel, duration)
        }
    }

    internal fun addLog(log: LogLine) {
        scope.launch(MainUIDispatcher) {
            logs.add(log)
        }
    }

    internal fun clearLogs() {
        scope.launch(MainUIDispatcher) {
            logs.clear()
        }
    }
}