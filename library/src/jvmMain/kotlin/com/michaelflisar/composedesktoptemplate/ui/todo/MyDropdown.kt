package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme

@Composable
fun <T> MyDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T & Any>,
    selected: MutableState<T>,
    mapper: (item: T & Any) -> String,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChanged: ((T & Any) -> Unit)? = null
) {
    val texts = items.map { mapper(it) }
    val selectedIndex = items.indexOf(selected.value)
    MyDropdownImpl(modifier, title, texts, selectedIndex, enabled, color, backgroundColor) { index, item ->
        val s = items[index]
        selected.value = s
        onSelectionChanged?.invoke(s)
    }
}

@Composable
fun <T> MyDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T & Any>,
    selected: T,
    mapper: (item: T & Any) -> String,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChanged: ((T & Any) -> Unit)? = null
) {
    val texts = items.map { mapper(it) }
    val selectedIndex = items.indexOf(selected)
    MyDropdownImpl(modifier, title, texts, selectedIndex, enabled, color, backgroundColor) { index, item ->
        onSelectionChanged?.invoke(items[index])
    }
}

// Sonderfall: Int Daten (index) + String Werte
@Composable
fun MyDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selected: MutableState<Int>,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChanged: ((Int) -> Unit)? = null
) {
    val selectedIndex = selected.value
    MyDropdownImpl(modifier, title, items, selectedIndex, enabled, color, backgroundColor) { index, item ->
        selected.value = index
        onSelectionChanged?.invoke(index)
    }
}

@Composable
fun MyDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selected: Int,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChanged: ((Int) -> Unit)? = null
) {
    MyDropdownImpl(modifier, title, items, selected, enabled, color, backgroundColor) { index, item ->
        onSelectionChanged?.invoke(index)
    }
}

@Composable
private fun MyDropdownImpl(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selected: Int,
    enabled: Boolean,
    color: Color,
    backgroundColor: Color,
    onSelectionChange: (index: Int, item: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) -180f else 0f)
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val borderColor = color.takeIf { it != Color.Unspecified }?.copy(alpha = ContentAlpha.disabled)
            ?: MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        val labelColor =
            color.takeIf { it != Color.Unspecified } ?: MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)

            Row(
                modifier = Modifier.fillMaxWidth()
                    .clip(MaterialTheme.shapes.small)
                    .border(1.dp, borderColor, MaterialTheme.shapes.small)
                    .background(backgroundColor)
                    .then(
                        if (enabled) {
                            Modifier.clickable {
                                expanded = !expanded
                            }
                        } else Modifier
                    )
                    .padding(AppTheme.ITEM_SPACING)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val style1 =
                    MaterialTheme.typography.body1.copy(fontSize = MaterialTheme.typography.body1.fontSize * .8f)
                val style2 = MaterialTheme.typography.body1.copy(fontSize = MaterialTheme.typography.body1.fontSize)
                Column(modifier = Modifier.weight(1f)) {
                    if (title.isNotEmpty()) {
                        Text(text = title, style = style1, fontWeight = FontWeight.Bold, color = labelColor)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Text(text = items.getOrElse(selected) { "" }, style = style2, color = color)
                }

                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = color
                )
            }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        onSelectionChange(index, item)
                        expanded = false
                    }
                ) {
                    Text(
                        text = item,
                        color = if (item == items.getOrElse(selected) { null }) MaterialTheme.colors.primary else Color.Unspecified,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}