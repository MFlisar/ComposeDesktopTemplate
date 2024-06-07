package com.michaelflisar.composedesktoptemplate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.settings.UISetting
import com.michaelflisar.composedesktoptemplate.ui.todo.MyScrollableLazyColumn

private val INSET_STEP = 4.dp

internal sealed class Info {

    interface InfoText {
        val text: String
        val subText: String
        val color: Color
    }

    class Title(
        override val text: String,
        override val subText: String = "",
        override val color: Color = Color.Unspecified
    ) : Info(), InfoText

    class Text(
        override val text: String,
        override val subText: String = "",
        override val color: Color = Color.Unspecified
    ) : Info(), InfoText

    data object InsetPlus : Info()
    data object InsetMinus : Info()
}

class InfoState internal constructor(
    internal val infos: SnapshotStateList<Info>
) {
    fun size() = infos.size

    fun addTitle(
        text: String,
        subText: String = "",
        color: Color = Color.Unspecified
    ) {
        infos.add(Info.Title(text, subText, color))
    }

    fun addText(
        text: String,
        subText: String = "",
        color: Color = Color.Unspecified
    ) {
        infos.add(Info.Text(text, subText, color))
    }

    fun insetIncrease() {
        infos.add(Info.InsetPlus)
    }

    fun insetDecrease() {
        infos.add(Info.InsetMinus)
    }

    internal fun list() = infos.toList()
}

@Composable
fun rememberInfos() = InfoState(
    remember { mutableStateListOf() }
)

@Composable
fun DesktopInfos(
    modifier: Modifier = Modifier,
    autoScroll: UISetting.Bool,
    infos: InfoState
) {
    val listState = rememberLazyListState()
    val appState = LocalAppState.current
    val autoScroll = autoScroll.getState(appState)
    LaunchedEffect(infos.size(), autoScroll.value) {
        if (infos.size() > 0 && autoScroll.value)
            listState.scrollToItem(infos.size() - 1)
    }
    val items = remember(infos.size()) { infos.list() }
    Column(modifier = modifier) {
        MyScrollableLazyColumn(
            Modifier.fillMaxWidth().weight(1f),
            AppTheme.ITEM_SPACING_MINI,
            state = listState
        ) {
            if (items.isEmpty()) {
                item(-1) {
                    Text(text = "Empty!", style = MaterialTheme.typography.body2)
                }
            } else {
                items.forEachIndexed { index, info ->
                    when (info) {
                        Info.InsetMinus,
                        Info.InsetPlus -> {
                        }

                        is Info.Text -> item(index) { InfoText(info, items.take(index)) }
                        is Info.Title -> item(index) { TitleLine(info, items.take(index)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoText(
    info: Info.Text,
    infosAbove: List<Info>
) {
    val style = MaterialTheme.typography.body2
    Line(info.text, info.subText, style, info.color, infosAbove)
}

@Composable
private fun TitleLine(
    info: Info.Title,
    infosAbove: List<Info>
) {
    val style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold)
    Line(info.text, info.subText, style, info.color, infosAbove)
}

@Composable
private fun Line(
    text: String,
    subText: String,
    style: TextStyle,
    color: Color,
    infosAbove: List<Info>
) {
    val countPlus = infosAbove.count { it is Info.InsetPlus }
    val countMinus = infosAbove.count { it is Info.InsetMinus }
    val inset = countPlus - countMinus
    Column(
        modifier = Modifier.padding(start = INSET_STEP * inset),
        verticalArrangement = Arrangement.spacedBy(AppTheme.ITEM_SPACING)
    ) {
        Text(
            text = text,
            style = style,
            color = color
        )
        if (subText.isNotEmpty()) {
            Text(
                text = subText,
                style = style,
                color = color
            )
        }
    }
}