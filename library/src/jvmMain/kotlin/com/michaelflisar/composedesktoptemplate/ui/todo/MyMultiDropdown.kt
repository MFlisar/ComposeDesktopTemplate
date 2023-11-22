package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Clear
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
fun <T> MyMultiDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    selected: MutableState<List<T>>,
    mapper: (item: T) -> String,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChange: ((List<T>) -> Unit)? = null
) {
    val texts = items.map { mapper(it) }
    val selectedIndizes = selected.value.map { items.indexOf(it) }
    MyMultiDropdown(
        modifier,
        title,
        texts,
        selectedIndizes,
        enabled,
        color,
        backgroundColor
    ) { selectedIndizes, selectedItems ->
        selected.value = selectedIndizes.map { items[it] }
        onSelectionChange?.invoke(selected.value)
    }
}

@Composable
fun <T> MyMultiDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    selected: List<T>,
    mapper: (item: T) -> String,
    enabled: Boolean = true,
    color: Color = Color.Unspecified,
    backgroundColor: Color = Color.Unspecified,
    onSelectionChange: (List<T>) -> Unit
) {
    val texts = items.map { mapper(it) }
    val selectedIndizes = selected.map { items.indexOf(it) }
    MyMultiDropdown(
        modifier,
        title,
        texts,
        selectedIndizes,
        enabled,
        color,
        backgroundColor
    ) { selectedIndizes, selectedItems ->
        onSelectionChange(selectedIndizes.map { items[it] })
    }
}

@Composable
private fun MyMultiDropdown(
    modifier: Modifier = Modifier,
    title: String,
    items: List<String>,
    selected: List<Int>,
    enabled: Boolean,
    color: Color,
    backgroundColor: Color,
    onSelectionChange: (indices: List<Int>, items: List<String>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) -180f else 0f)
    var selectedItems = selected.map { items[it] }
    val empty = remember(selectedItems) { derivedStateOf { selectedItems.isEmpty() } }
    Box(
        modifier = modifier//.padding(horizontal = 16.dp)
    ) {
        val borderColor = color.takeIf { it != Color.Unspecified }?.copy(alpha = ContentAlpha.disabled)
            ?: MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        val labelColor =
            color.takeIf { it != Color.Unspecified } ?: MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
        ) {
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
                //.padding(horizontal = 16.dp, vertical = 8.dp)
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
                    Text(text = selectedItems.joinToString(";"), style = style2, color = color)
                }

                Icon(
                    modifier = Modifier.rotate(rotation),
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = color
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Button(
                modifier = Modifier
                    .padding(AppTheme.ITEM_SPACING)
                    .fillMaxWidth(),
                onClick = {
                    onSelectionChange(emptyList(), emptyList())
                },
                enabled = !empty.value
            ) {
                Icon(
                    modifier = Modifier.align(Alignment.CenterVertically).padding(end = AppTheme.ITEM_SPACING),
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
                Text(
                    text = "Clear Selection",
                    modifier = Modifier.align(Alignment.CenterVertically).weight(1f)
                )
            }
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        val selectedNew = selected.toMutableList()
                        val selectedItemsNew = selectedItems.toMutableList()
                        if (selected.contains(index)) {
                            selectedNew.remove(index)
                            selectedItemsNew.remove(item)
                        } else {
                            selectedNew.add(index)
                            selectedItemsNew.add(item)
                        }
                        onSelectionChange(selectedNew, selectedItemsNew)
                        //expanded = false
                    }
                ) {
                    val selected = selectedItems.contains(item)
                    val color = if (selected) MaterialTheme.colors.primary else Color.Unspecified
                    Icon(
                        modifier = Modifier.align(Alignment.CenterVertically).padding(end = AppTheme.ITEM_SPACING),
                        imageVector = if (selected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription = null,
                        tint = color
                    )
                    Text(
                        text = item,
                        color = color,
                        modifier = Modifier.align(Alignment.CenterVertically).fillMaxWidth()
                    )
                }
            }
        }
    }
}