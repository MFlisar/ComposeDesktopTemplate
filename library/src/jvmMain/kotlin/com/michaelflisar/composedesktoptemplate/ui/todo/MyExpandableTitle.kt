package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.settings.UISetting

@Composable
fun MyExpandableTitle(
    text: String,
    setting: UISetting.Bool,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    showIcon: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val appState = LocalAppState.current
    val visible = setting.getState(appState).value
    MyExpandableTitle(
        text,
        visible,
        onToggle = { setting.updateValue(appState, !visible) },
        modifier,
        color,
        showIcon,
        content
    )
}

@Composable
fun MyExpandableTitle(
    text: String,
    visible: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    showIcon: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    MyExpandableTitle(
        text,
        visible.value,
        { visible.value = !visible.value },
        modifier,
        color,
        showIcon,
        content
    )
}

@Composable
fun MyExpandableTitle(
    text: String,
    visible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    showIcon: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val rotation by animateFloatAsState(if (visible) -180f else 0f)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.small)
                .clickable {
                    onToggle()
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showIcon) {
                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    tint = color.takeIf { it != Color.Unspecified }
                        ?: LocalContentColor.current.copy(alpha = LocalContentAlpha.current),
                    contentDescription = null
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        AnimatedVisibility(visible = visible) {
            Column {
                content()
            }
        }
    }
}