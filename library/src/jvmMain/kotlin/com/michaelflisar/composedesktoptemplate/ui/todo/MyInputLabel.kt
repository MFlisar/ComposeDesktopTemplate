package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun MyInputLabel(
    label: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    Text(modifier = modifier,text = label, fontWeight = FontWeight.Bold, color = color)
}