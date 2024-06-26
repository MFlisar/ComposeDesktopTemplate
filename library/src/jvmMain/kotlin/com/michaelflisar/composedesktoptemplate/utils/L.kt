package com.michaelflisar.composedesktoptemplate.utils

import com.michaelflisar.composedesktoptemplate.classes.AppState
import com.michaelflisar.composedesktoptemplate.internal.LogLine
import com.michaelflisar.composedesktoptemplate.internal.InfoType
import java.util.regex.Pattern

object L {

    var ADVANCED_LOGGING = false

    fun d(info: String, success: Boolean = false) {
        log(info, if (success) InfoType.DebugSuccess else InfoType.Debug)
    }

    fun e(info: String) {
        log(info, InfoType.Error)
    }

    fun e(e: Exception) {
        log(e.message ?: "ERROR", InfoType.Error)
    }

    internal fun registerAppState(state: AppState) {
        this.appState = state
    }

    // -----------------
    // private functions
    // -----------------

    private var appState: AppState? = null

    private val ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$")

    private fun getCaller(index: Int): String {
        val stackTrace = Throwable().stackTrace.toList()
        var element = getElement(stackTrace, index)
        var element2: StackTraceElement? = null

        if (ADVANCED_LOGGING) {
            // functions can not start with numbers, so of the class name ends with a number, this must be a lambda which is handled as anonymous function
            val split = element?.className?.split("$")
            if (split?.lastOrNull()?.toIntOrNull() != null) {
                // example from demo:
                // com.michaelflisar.lumberjack.demo.MainActivity$onCreate$func$1
                // => so we need to go up +2, because we want to get the line in onCreate where the caller executes the lambda
                element2 = element
                element = getElement(stackTrace, index + 2)
            }
        }

        val info = getLink(element, element2)
        return info
    }

    private fun getLink(element: StackTraceElement?, element2: StackTraceElement?): String {
        var link = "${element?.fileName}:${element?.lineNumber}"
        // AndroidStudio does not support 2 clickable links...
        element2?.let {
            link += " (${it.fileName}:${it.lineNumber})"
        }
        return link
    }

    private fun getElement(stackTrace: List<StackTraceElement>, index: Int): StackTraceElement? {
        if (stackTrace.isEmpty())
            return null
        var i = index
        if (index >= stackTrace.size) {
            i = stackTrace.size - 1
        }
        return stackTrace[i]
    }

    private fun getClassName(element: StackTraceElement?): String {
        if (element == null)
            return ""
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        return tag
    }

    private fun log(
        info: String,
        type: InfoType = InfoType.Debug,
        exception: Exception? = null
    ) {
        val caller = getCaller(5)
        val info = LogLine(caller, info, type = type, exception = exception)
        appState?.addLog(info)
        println(info.getConsoleLog())
    }
}