package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

@Composable
fun DesktopPaneCenter(
    modifier: Modifier,
    applyPadding: Boolean = true,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.then(if (applyPadding) Modifier.padding(AppTheme.CONTENT_PADDING_SMALL) else Modifier)
    ) {
        content()
    }
}