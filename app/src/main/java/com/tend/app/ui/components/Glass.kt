package com.tend.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tend.app.data.Priority
import com.tend.app.ui.theme.TendColors

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    corner: Int = 24,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(6.dp, RoundedCornerShape(corner.dp))
            .clip(RoundedCornerShape(corner.dp))
            .background(Brush.verticalGradient(listOf(TendColors.GlassTop, TendColors.GlassBottom)))
            .border(1.dp, TendColors.GlassBorder, RoundedCornerShape(corner.dp))
            .padding(18.dp),
        content = content
    )
}

@Composable
fun AppBackground(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .background(
                Brush.radialGradient(
                    colors = listOf(MaterialTheme.colorScheme.secondary, Color.Transparent),
                    radius = 1100f
                )
            )
    ) {
        content()
    }
}

@Composable
fun Pill(label: String, selected: Boolean, onClick: () -> Unit) {
    val fill = if (selected) MaterialTheme.colorScheme.primary else Color(0x26FFFFFF)
    val labelColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(fill)
            .border(1.dp, if (selected) Color.Transparent else TendColors.GlassBorder, RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = labelColor)
    }
}

fun priorityColor(priority: Priority): Color = when (priority) {
    Priority.HIGH -> TendColors.Rose
    Priority.MEDIUM -> TendColors.Amber
    Priority.LOW -> TendColors.Emerald
}
