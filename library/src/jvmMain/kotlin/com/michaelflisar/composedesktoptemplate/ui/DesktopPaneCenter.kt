package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

@Composable
fun DesktopPaneCenter(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.padding(AppTheme.CONTENT_PADDING_SMALL)
    ) {
        content()
    }
}