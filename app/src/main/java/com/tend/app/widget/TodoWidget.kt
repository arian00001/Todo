package com.tend.app.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontFamily
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.tend.app.MainActivity
import com.tend.app.data.Store

class TodoWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        Store.init(context)
        provideContent { WidgetBody() }
    }
}

@Composable
private fun WidgetBody() {
    val open = Store.openTaskCount()
    val preview = Store.openTaskPreview(3)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color(0xFFFAF7F2))
            .padding(14.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        Text(
            text = "Tend",
            style = TextStyle(
                color = ColorProvider(Color(0xFF1B1A28)),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SERIF
            )
        )
        Text(
            text = if (open == 0) "All clear" else open.toString() + " open",
            style = TextStyle(color = ColorProvider(Color(0xFF5B5872)), fontSize = 12.sp)
        )
        Spacer(GlanceModifier.height(8.dp))
        if (preview.isEmpty()) {
            Text(
                text = "Nothing on the board",
                style = TextStyle(color = ColorProvider(Color(0xFF5B5872)), fontSize = 12.sp)
            )
        } else {
            preview.forEach { item ->
                Text(
                    text = "- " + item,
                    style = TextStyle(color = ColorProvider(Color(0xFF1B1A28)), fontSize = 12.sp),
                    maxLines = 1
                )
            }
        }
    }
}
