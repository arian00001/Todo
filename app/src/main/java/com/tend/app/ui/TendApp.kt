package com.tend.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tend.app.TendViewModel
import com.tend.app.data.Note
import com.tend.app.data.Task
import com.tend.app.ui.components.AppBackground

@Composable
fun TendApp(viewModel: TendViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    var tab by remember { mutableIntStateOf(0) }
    var editingTask by remember { mutableStateOf<Task?>(null) }
    var editingNote by remember { mutableStateOf<Note?>(null) }

    val task = editingTask
    if (task != null) {
        EditTaskDialog(
            task = task,
            onDismiss = { editingTask = null },
            onSave = { viewModel.updateTask(it); editingTask = null },
            onDelete = { viewModel.deleteTask(it); editingTask = null }
        )
    }

    val note = editingNote
    if (note != null) {
        NoteEditorScreen(
            note = note,
            onClose = { editingNote = null },
            onSave = { viewModel.saveNote(it); editingNote = null },
            onDelete = { viewModel.deleteNote(it); editingNote = null }
        )
        return
    }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(containerColor = Color(0xF2FFFFFF)) {
                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(Icons.Filled.Check, contentDescription = null) },
                    label = { Text("Tasks") }
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(Icons.Filled.Create, contentDescription = null) },
                    label = { Text("Notes") }
                )
            }
        }
    ) { inner ->
        AppBackground(modifier = Modifier.padding(inner)) {
            if (tab == 0) {
                TasksScreen(
                    tasks = tasks,
                    onAdd = { title, detail, priority, due -> viewModel.addTask(title, detail, priority, due) },
                    onToggleDone = { viewModel.toggleDone(it) },
                    onDelete = { viewModel.deleteTask(it) },
                    onEdit = { editingTask = it }
                )
            } else {
                NotesScreen(
                    notes = notes,
                    onCreate = { editingNote = viewModel.newNote() },
                    onOpen = { editingNote = it },
                    onTogglePin = { viewModel.togglePinned(it) },
                    onDelete = { viewModel.deleteNote(it) }
                )
            }
        }
    }
}
