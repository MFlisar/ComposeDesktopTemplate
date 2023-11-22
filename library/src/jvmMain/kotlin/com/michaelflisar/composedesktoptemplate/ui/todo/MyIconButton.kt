package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyIconButton(
    modifier: Modifier,
    icon: ImageVector,
    iconTint: Color = Color.Unspecified,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(icon, null, tint = iconTint)
    }
}