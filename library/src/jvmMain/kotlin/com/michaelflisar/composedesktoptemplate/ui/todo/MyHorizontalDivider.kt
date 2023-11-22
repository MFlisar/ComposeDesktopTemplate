package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

@Composable
fun ColumnScope.MyHorizontalDivider(modifier: Modifier = Modifier, size: Dp = AppTheme.DIVIDER_SIZE, color: Color = MaterialTheme.colors.onBackground) {
    Divider(
        modifier = modifier.height(size),//.fillMaxWidth(),
        color = color
    )
}