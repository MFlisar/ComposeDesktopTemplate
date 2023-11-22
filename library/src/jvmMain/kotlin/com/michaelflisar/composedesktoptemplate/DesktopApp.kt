package com.michaelflisar.composedesktoptemplate

import androidx.compose.ui.window.WindowPlacement
import com.michaelflisar.composedesktoptemplate.settings.UISetting

object DesktopApp {

    internal object Constants {
        val WINDOW_WIDTH = UISetting.Integer("window_width", "", 1024)
        val WINDOW_HEIGHT = UISetting.Integer("window_height", "", 800)
        val WINDOW_X = UISetting.Integer("window_x", "", 0)
        val WINDOW_Y = UISetting.Integer("window_y", "", 0)
        val WINDOW_PLACEMENT = UISetting.Integer("window_placement", "", WindowPlacement.Floating.ordinal)
    }

}