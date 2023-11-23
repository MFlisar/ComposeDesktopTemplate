package com.michaelflisar.composedesktoptemplate.demo

import com.michaelflisar.composedesktoptemplate.settings.UISetting

object Settings {

    val EXPANDED_LEFT_PANE = UISetting.Bool("ExpandedLeftPane", "", true)
    val EXPANDED_RIGHT_PANE = UISetting.Bool("ExpandedRightPane", "", true)
    val AUTO_SCROLL_LOGS = UISetting.Bool("AutoScrollLogs", "", true)

    val AUTO_OPEN_LOG_PAGE_ON_NEW_LOG_ERROR = UISetting.Bool("AutoOpenLogPageOnError", "Log Page bei ERROR autom. öffnen", true)

    val ALL = listOf(AUTO_OPEN_LOG_PAGE_ON_NEW_LOG_ERROR)
}