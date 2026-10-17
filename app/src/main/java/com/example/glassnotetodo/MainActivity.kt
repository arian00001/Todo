package com.example.glassnotetodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Ink = Color(0xFF202938)
private val Muted = Color(0xFF788393)
private val Accent = Color(0xFF667DE8)
private val Glass = Color(0xCFFFFFFF)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GlassNoteApp() }
    }
}

@Composable
fun GlassNoteApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    var tab by remember { mutableStateOf(1) }
    var notes by remember { mutableStateOf(Store.notes(context)) }
    var todos by remember { mutableStateOf(Store.todos(context)) }
    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var noteTitle by remember { mutableStateOf("") }
    var noteBody by remember { mutableStateOf("") }
    var newTodo by remember { mutableStateOf("") }

    fun refreshNotes() { notes = Store.notes(context) }
    fun refreshTodos() { todos = Store.todos(context) }

    MaterialTheme(colorScheme = lightColorScheme(primary = Accent, background = Color(0xFFF5F7FB))) {
        Box(
            Modifier.fillMaxSize().background(
                Brush.linearGradient(listOf(Color(0xFFF9FBFF), Color(0xFFEAF0FF), Color(0xFFF8F5FF)))
            )
        ) {
            Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("SUNDAY, YOUR SPACE", fontSize = 10.sp, color = Muted, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Text("A little more clarity.", fontSize = 27.sp, color = Ink, fontWeight = FontWeight.Bold)
                        Text("Notes and tasks, beautifully together.", fontSize = 13.sp, color = Muted)
                    }
                    Box(
                        Modifier.size(46.dp).background(Glass, CircleShape).border(1.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) { Text("✦", color = Accent, fontSize = 24.sp) }
                }
                Spacer(Modifier.height(22.dp))
                Row(
                    Modifier.fillMaxWidth().background(Color(0x99FFFFFF), RoundedCornerShape(20.dp)).padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    TabChip("To-do", tab == 1, Modifier.weight(1f), { tab = 1 }, "☑")
                    TabChip("Notes", tab == 0, Modifier.weight(1f), { tab = 0 }, "✎")
                }
                Spacer(Modifier.height(18.dp))
                if (tab == 1) {
                    val doneCount = todos.count { it.done }
                    GlassCard(Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("YOUR FOCUS", color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                                Text("${todos.size - doneCount} tasks left", color = Ink, fontSize = 23.sp, fontWeight = FontWeight.Bold)
                                Text(if (todos.isEmpty()) "Start with one small win." else "$doneCount of ${todos.size} completed", color = Muted, fontSize = 12.sp)
                            }
                            Box(Modifier.size(48.dp).background(Color(0xFFECEFFF), CircleShape), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.TaskAlt, null, tint = Accent)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        InputField(newTodo, { newTodo = it }, "Add a task…", Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (newTodo.isNotBlank()) {
                                    todos = (todos + Todo(System.currentTimeMillis(), newTodo.trim(), false, System.currentTimeMillis()))
                                    Store.saveTodos(context, todos); newTodo = ""; refreshTodos()
                                }
                            },
                            modifier = Modifier.size(48.dp).background(Accent, RoundedCornerShape(16.dp))
                        ) { Icon(Icons.Default.Add, null, tint = Color.White) }
                    }
                    Spacer(Modifier.height(10.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(todos, key = { it.id }) { todo ->
                            GlassCard(Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        Modifier.size(25.dp).background(if (todo.done) Accent else Color.Transparent, CircleShape)
                                            .border(1.5.dp, if (todo.done) Accent else Color(0xFFD1D8E5), CircleShape)
                                            .clickable {
                                                todos = todos.map { if (it.id == todo.id) it.copy(done = !it.done) else it }
                                                Store.saveTodos(context, todos); refreshTodos()
                                            },
                                        contentAlignment = Alignment.Center
                                    ) { if (todo.done) Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp)) }
                                    Spacer(Modifier.width(12.dp))
                                    Text(todo.text, Modifier.weight(1f), color = if (todo.done) Muted else Ink, fontSize = 14.sp,
                                        textDecoration = if (todo.done) TextDecoration.LineThrough else TextDecoration.None)
                                    IconButton(onClick = { todos = todos.filterNot { it.id == todo.id }; Store.saveTodos(context, todos); refreshTodos() }) {
                                        Icon(Icons.Default.Delete, null, tint = Color(0xFFADB5C3), modifier = Modifier.size(19.dp))
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Your notes", Modifier.weight(1f), color = Ink, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Button(onClick = { editingNote = null; noteTitle = ""; noteBody = ""; showEditor = true }, shape = RoundedCornerShape(14.dp)) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(17.dp)); Spacer(Modifier.width(4.dp)); Text("New")
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    if (notes.isEmpty()) {
                        GlassCard(Modifier.fillMaxWidth()) {
                            Text("A clean page awaits.", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Ink)
                            Text("Save ideas, thoughts, and little reminders here.", color = Muted, fontSize = 13.sp)
                        }
                    }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                        items(notes, key = { it.id }) { note ->
                            GlassCard(Modifier.fillMaxWidth().clickable {
                                editingNote = note; noteTitle = note.title; noteBody = note.body; showEditor = true
                            }) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Column(Modifier.weight(1f)) {
                                        Text(note.title.ifBlank { "Untitled note" }, color = Ink, fontSize = 17.sp, fontWeight = FontWeight.Bold)
                                        Spacer(Modifier.height(5.dp))
                                        Text(note.body, color = Muted, fontSize = 13.sp, maxLines = 4)
                                        Spacer(Modifier.height(9.dp))
                                        Text(SimpleDateFormat("MMM d · h:mm a", Locale.getDefault()).format(Date(note.updatedAt)), color = Color(0xFFA0A9B7), fontSize = 10.sp)
                                    }
                                    IconButton(onClick = { editingNote = note; noteTitle = note.title; noteBody = note.body; showEditor = true }) {
                                        Icon(Icons.Default.Edit, null, tint = Accent, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { notes = notes.filterNot { it.id == note.id }; Store.saveNotes(context, notes); refreshNotes() }) {
                                        Icon(Icons.Default.Delete, null, tint = Color(0xFFADB5C3), modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showEditor) {
                AlertDialog(
                    onDismissRequest = { showEditor = false },
                    title = { Text(if (editingNote == null) "New note" else "Edit note") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            InputField(noteTitle, { noteTitle = it }, "Title", Modifier.fillMaxWidth())
                            InputField(noteBody, { noteBody = it }, "Write your thoughts…", Modifier.fillMaxWidth(), singleLine = false)
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (noteTitle.isNotBlank() || noteBody.isNotBlank()) {
                                val id = editingNote?.id ?: System.currentTimeMillis()
                                val updated = Note(id, noteTitle.trim(), noteBody.trim(), System.currentTimeMillis())
                                notes = if (editingNote == null) listOf(updated) + notes else notes.map { if (it.id == id) updated else it }
                                Store.saveNotes(context, notes); refreshNotes()
                            }
                            showEditor = false
                        }) { Text("Save") }
                    },
                    dismissButton = { TextButton(onClick = { showEditor = false }) { Text("Cancel") } }
                )
            }
        }
    }
}

@Composable
private fun TabChip(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit, symbol: String) {
    Row(
        modifier.background(if (selected) Color.White else Color.Transparent, RoundedCornerShape(15.dp))
            .clickable(onClick = onClick).padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically
    ) {
        Text(symbol, color = if (selected) Accent else Muted, fontSize = 15.sp)
        Spacer(Modifier.width(7.dp))
        Text(label, color = if (selected) Ink else Muted, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier.shadow(8.dp, RoundedCornerShape(24.dp))
            .background(Glass, RoundedCornerShape(24.dp))
            .border(1.dp, Color.White.copy(alpha = 0.95f), RoundedCornerShape(24.dp))
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun InputField(value: String, onValueChange: (String) -> Unit, hint: String, modifier: Modifier = Modifier, singleLine: Boolean = true) {
    Row(
        modifier.shadow(2.dp, RoundedCornerShape(16.dp))
            .background(Color(0xEFFFFFFF), RoundedCornerShape(16.dp))
            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.weight(1f)) {
            if (value.isEmpty()) Text(hint, color = Color(0xFFA5ADBA), fontSize = 14.sp)
            BasicTextField(
                value = value, onValueChange = onValueChange, singleLine = singleLine,
                textStyle = androidx.compose.ui.text.TextStyle(color = Ink, fontSize = 14.sp),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
