package com.michaelflisar.composedesktoptemplate.ui.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.michaelflisar.composedesktoptemplate.internal.InfoType
import com.michaelflisar.composedesktoptemplate.classes.LocalAppState
import com.michaelflisar.composedesktoptemplate.classes.Status
import com.michaelflisar.composedesktoptemplate.classes.AppTheme
import com.michaelflisar.composedesktoptemplate.ui.todo.MyHorizontalDivider
import com.michaelflisar.composedesktoptemplate.ui.todo.MyVerticalDivider

@Composable
internal fun StatusBar(
    footer: (@Composable (modifier: Modifier) -> Unit)? = null
) {
    val appState = LocalAppState.current
    val state = appState.state.value

    val infos = appState.logs.count { it.type != InfoType.Error }
    val errors = appState.logs.count { it.type == InfoType.Error }

    val (infoState, running) = when (state) {
        Status.None -> Pair("", false)
        is Status.Running -> Pair(state.label, true)
    }

    val mod = Modifier.padding(horizontal = AppTheme.CONTENT_PADDING_SMALL, vertical = 2.dp)
    val style = MaterialTheme.typography.body2

    //AuroraDecorationArea(decorationAreaType = DecorationAreaType.Footer) {
       Column {
           MyHorizontalDivider(color = LocalContentColor.current)
           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   //.auroraBackground()
                   .background(MaterialTheme.colors.onBackground)
                   .height(IntrinsicSize.Min),
               //horizontalArrangement = Arrangement.spacedBy(MyAppTheme.ITEM_SPACING_MINI),
               verticalAlignment = Alignment.CenterVertically
           ) {
               CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.background) {
               if (infoState.isNotEmpty()) {
                   Text(modifier = mod, text = infoState, style = style)
               }
               if (running) {
                   LinearProgressIndicator(
                       modifier = Modifier.width(128.dp).padding(horizontal = AppTheme.ITEM_SPACING)
                   )
               }
               footer?.invoke(Modifier.weight(1f)) ?: Spacer(modifier = Modifier.weight(1f))
               MyVerticalDivider(color = LocalContentColor.current)
               Text(modifier = mod, text = "Infos: $infos", style = style, fontWeight = FontWeight.Bold)
               MyVerticalDivider(color = LocalContentColor.current)
               Text(
                   modifier = mod,
                   text = "Errors: $errors",
                   style = style,
                   fontWeight = FontWeight.Bold,
                   color = if (errors > 0) MaterialTheme.colors.error else Color.Unspecified
               )
               }
           }
      // }
    }
}