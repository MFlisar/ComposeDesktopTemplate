package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

@Composable
fun RowScope.MyVerticalDivider(modifier: Modifier = Modifier, size: Dp = AppTheme.DIVIDER_SIZE, color: Color = MaterialTheme.colors.onBackground) {
    Divider(modifier = modifier.width(size).fillMaxHeight(), color = color)
}