package com.michaelflisar.composedesktoptemplate.classes

sealed class Status {
    data object None : Status()
    class Running(val label: String) : Status()
}