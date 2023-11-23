package com.michaelflisar.composedesktoptemplate.utils

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

object Util {

    fun setClipboard(s: String) {
        val selection = StringSelection(s)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }
}