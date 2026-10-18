package com.tend.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tend.app.data.Note
import com.tend.app.ui.components.AppBackground
import com.tend.app.ui.theme.TendColors

@Composable
fun NoteEditorScreen(
    note: Note,
    onClose: () -> Unit,
    onSave: (Note) -> Unit,
    onDelete: (String) -> Unit
) {
    var title by remember(note.id) { mutableStateOf(note.title) }
    var body by remember(note.id) { mutableStateOf(note.body) }
    var pinned by remember(note.id) { mutableStateOf(note.pinned) }

    fun save() {
        onSave(note.copy(title = title.trim(), body = body, pinned = pinned))
    }

    BackHandler { save() }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize().imePadding().padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { save() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Save and go back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Text(
                    text = "NOTE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { pinned = !pinned }) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Pin note",
                        tint = if (pinned) TendColors.Amber else MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = { onDelete(note.id) }) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete note",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title") },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                placeholder = { Text("Write something worth keeping") },
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }
}
