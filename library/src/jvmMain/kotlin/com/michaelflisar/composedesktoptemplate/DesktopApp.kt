package com.michaelflisar.composedesktoptemplate

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.WindowPlacement
import com.michaelflisar.composedesktoptemplate.settings.UISetting

object DesktopApp {

    internal object Constants {
        val WINDOW_WIDTH = UISetting.Integer("window_width", "", 1024)
        val WINDOW_HEIGHT = UISetting.Integer("window_height", "", 800)
        val WINDOW_X = UISetting.Integer("window_x", "", 0)
        val WINDOW_Y = UISetting.Integer("window_y", "", 0)
        val WINDOW_PLACEMENT = UISetting.Integer("window_placement", "", WindowPlacement.Floating.ordinal)

        val COLORS = lightColors(
            primary = Color(0xff1976d2),
            primaryVariant = Color(0xffc1dcf7),
            secondary = Color(0xff00c853),
            secondaryVariant = Color(0xff91f5ba)
        )
    }

}