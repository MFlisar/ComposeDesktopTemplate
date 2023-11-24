package com.michaelflisar.composedesktoptemplate.ui.todo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun <T> MySegmentedControl(
    modifier: Modifier = Modifier,
    items: List<T & Any>,
    selected: MutableState<T>,
    mapper: (item: T & Any) -> String,
    color: Color = Color.Unspecified,
    onSelectionChanged: ((T) -> Unit)? = null
) {
    val texts = items.map { mapper(it) }
    val selectedIndex = items.indexOf(selected.value)
    MySegmentedControlImpl(modifier, texts, selectedIndex, color) { index, item ->
        val s = (if (index >= 0) items[index] else null) as T
        selected.value = s
        onSelectionChanged?.invoke(s)
    }
}

@Composable
fun <T> MySegmentedControl(
    modifier: Modifier = Modifier,
    items: List<T & Any>,
    selected: T,
    mapper: (item: T & Any) -> String,
    color: Color = Color.Unspecified,
    onSelectionChanged: ((T) -> Unit)? = null
) {
    val texts = items.map { mapper(it) }
    val selectedIndex = items.indexOf(selected)
    MySegmentedControlImpl(modifier, texts, selectedIndex, color) { index, item ->
        onSelectionChanged?.invoke(items[index])
    }
}

// Sonderfall: Int Daten (index) + String Werte
@Composable
fun MySegmentedControl(
    modifier: Modifier = Modifier,
    items: List<String>,
    selected: MutableState<Int>,
    color: Color = Color.Unspecified,
    onSelectionChanged: ((Int) -> Unit)? = null
) {
    val selectedIndex = selected.value
    MySegmentedControlImpl(modifier, items, selectedIndex, color) { index, item ->
        selected.value = index
        onSelectionChanged?.invoke(index)
    }
}

@Composable
fun MySegmentedControl(
    modifier: Modifier = Modifier,
    items: List<String>,
    selected: Int,
    color: Color = Color.Unspecified,
    onSelectionChanged: ((Int) -> Unit)? = null
) {
    MySegmentedControlImpl(modifier, items, selected, color) { index, item ->
        onSelectionChanged?.invoke(index)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MySegmentedControlImpl(
    modifier: Modifier = Modifier,
    items: List<String>,
    selected: Int,
    color: Color,
    onSelectionChange: (index: Int, item: String?) -> Unit
) {
    val borderColor = color.takeIf { it != Color.Unspecified }?.copy(alpha = ContentAlpha.disabled)
        ?: MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)

    val colorSelected = color.takeIf { it != Color.Unspecified } ?: MaterialTheme.colors.primary
    val colorSelectedText = MaterialTheme.colors.contentColorFor(colorSelected)

    val colorNotSelected = Color.Unspecified
    val colorNotSelectedText = Color.Unspecified

    Column(
        modifier = modifier
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEachIndexed { index, item ->
                Text(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .border(1.dp, borderColor, MaterialTheme.shapes.small)
                        .background(if (selected == index) colorSelected else colorNotSelected)
                        .clickable {
                            if (selected == index)
                                onSelectionChange(-1, null)
                            else
                                onSelectionChange(index, item)
                        }
                        .widthIn(min = 40.dp)
                        .padding(4.dp),
                    text = item,
                    maxLines = 1,
                    color = if (selected == index) colorSelectedText else colorNotSelectedText
                )
            }
        }
    }
}