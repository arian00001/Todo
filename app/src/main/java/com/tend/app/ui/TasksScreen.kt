package com.tend.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.tend.app.data.Priority
import com.tend.app.data.Task
import com.tend.app.data.TaskStatus
import com.tend.app.ui.components.GlassCard
import com.tend.app.ui.components.Pill
import com.tend.app.ui.components.priorityColor
import com.tend.app.ui.theme.TendColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d", Locale.US)

private fun prettyDate(value: String): String =
    runCatching { LocalDate.parse(value).format(DATE_FORMAT) }.getOrDefault(value)

private fun isOverdue(value: String): Boolean =
    runCatching { LocalDate.parse(value).isBefore(LocalDate.now()) }.getOrDefault(false)

@Composable
fun TasksScreen(
    tasks: List<Task>,
    onAdd: (String, String, Priority, String) -> Unit,
    onToggleDone: (String) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (Task) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<TaskStatus?>(null) }
    var priorityFilter by remember { mutableStateOf<Priority?>(null) }

    val visible = tasks
        .filter { task ->
            (statusFilter == null || task.status == statusFilter) &&
                (priorityFilter == null || task.priority == priorityFilter) &&
                (query.isBlank() || task.title.contains(query.trim(), true) || task.notes.contains(query.trim(), true))
        }
        .sortedWith(compareBy({ it.isDone }, { -it.createdAt }))

    LazyColumn(
        modifier = Modifier.fillMaxSize().imePadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ScreenHeader() }
        item { StatsCard(tasks) }
        item { AddTaskCard(onAdd) }
        item { SearchField(query) { query = it } }
        item { FilterRow(statusFilter, priorityFilter, { statusFilter = it }, { priorityFilter = it }) }
        if (visible.isEmpty()) {
            item { EmptyCard(statusFilter != null || priorityFilter != null || query.isNotBlank()) }
        } else {
            items(visible, key = { it.id }) { task ->
                TaskRow(task, onToggleDone, onDelete, onEdit)
            }
        }
    }
}

@Composable
private fun ScreenHeader() {
    Column(modifier = Modifier.padding(bottom = 2.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Tend",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "TASK BOARD",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(18.dp))
        Text(
            text = "Everything you mean to do.",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Add a task, move it along, let it go. A small board that stays out of the way.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatsCard(tasks: List<Task>) {
    val todo = tasks.count { it.status == TaskStatus.TODO }
    val doing = tasks.count { it.status == TaskStatus.IN_PROGRESS }
    val done = tasks.count { it.status == TaskStatus.DONE }
    val total = tasks.size
    val progress = if (total == 0) 0f else done.toFloat() / total.toFloat()
    val percent = (progress * 100).toInt()

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            StatCell("To do", todo, Modifier.weight(1f))
            StatCell("In progress", doing, Modifier.weight(1f))
            StatCell("Done", done, Modifier.weight(1f))
        }
        Spacer(Modifier.height(18.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0x1F1B1A28))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (total == 0) "Nothing on the board yet" else percent.toString() + "% complete - " + total.toString() + " tasks",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatCell(label: String, value: Int, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun AddTaskCard(onAdd: (String, String, Priority, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var due by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var details by remember { mutableStateOf(false) }

    fun submit() {
        if (title.isBlank()) return
        onAdd(title.trim(), notes.trim(), priority, due.trim())
        title = ""
        notes = ""
        due = ""
        priority = Priority.MEDIUM
        details = false
    }

    GlassCard(modifier = Modifier.fillMaxWidth(), corner = 26) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("What needs doing?") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(10.dp))
            IconButton(onClick = { submit() }, enabled = title.isNotBlank()) {
                Icon(Icons.Filled.Add, contentDescription = "Add task")
            }
        }
        TextButton(onClick = { details = !details }) {
            Text(if (details) "Hide details" else "Add notes, due date, priority")
        }
        if (details) {
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Any detail worth remembering") },
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(
                value = due,
                onValueChange = { due = it },
                placeholder = { Text("Due date 2026-10-24") },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Pill("High", priority == Priority.HIGH) { priority = Priority.HIGH }
                Pill("Medium", priority == Priority.MEDIUM) { priority = Priority.MEDIUM }
                Pill("Low", priority == Priority.LOW) { priority = Priority.LOW }
            }
        }
    }
}

@Composable
private fun SearchField(value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("Search tasks") },
        singleLine = true,
        shape = RoundedCornerShape(999.dp),
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FilterRow(
    status: TaskStatus?,
    priority: Priority?,
    onStatus: (TaskStatus?) -> Unit,
    onPriority: (Priority?) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("All", status == null) { onStatus(null) }
            Pill("To do", status == TaskStatus.TODO) { onStatus(TaskStatus.TODO) }
            Pill("Doing", status == TaskStatus.IN_PROGRESS) { onStatus(TaskStatus.IN_PROGRESS) }
            Pill("Done", status == TaskStatus.DONE) { onStatus(TaskStatus.DONE) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Pill("Any", priority == null) { onPriority(null) }
            Pill("High", priority == Priority.HIGH) { onPriority(Priority.HIGH) }
            Pill("Medium", priority == Priority.MEDIUM) { onPriority(Priority.MEDIUM) }
            Pill("Low", priority == Priority.LOW) { onPriority(Priority.LOW) }
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onToggleDone: (String) -> Unit,
    onDelete: (String) -> Unit,
    onEdit: (Task) -> Unit
) {
    var menu by remember { mutableStateOf(false) }
    val late = task.dueDate.isNotBlank() && !task.isDone && isOverdue(task.dueDate)

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (task.isDone) MaterialTheme.colorScheme.primary else Color(0x1FFFFFFF))
                    .border(1.dp, if (task.isDone) Color.Transparent else MaterialTheme.colorScheme.outline, CircleShape)
                    .clickable { onToggleDone(task.id) },
                contentAlignment = Alignment.Center
            ) {
                if (task.isDone) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "Mark as to do",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f).clickable { onEdit(task) }) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (task.isDone) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                    textDecoration = if (task.isDone) TextDecoration.LineThrough else TextDecoration.None
                )
                if (task.notes.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = task.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3
                    )
                }
                Spacer(Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(priorityColor(task.priority))
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = task.priority.label,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = task.status.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (task.dueDate.isNotBlank()) {
                        Text(
                            text = if (late) "Overdue - " + prettyDate(task.dueDate) else prettyDate(task.dueDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (late) TendColors.Rose else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Box {
                IconButton(onClick = { menu = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Task options",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DropdownMenu(expanded = menu, onDismissRequest = { menu = false }) {
                    DropdownMenuItem(
                        text = { Text("Edit task") },
                        onClick = { menu = false; onEdit(task) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete task") },
                        onClick = { menu = false; onDelete(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCard(filtered: Boolean) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (filtered) "Nothing matches these filters" else "The board is clear",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = if (filtered) "Try another status or clear the search." else "Add your first task above.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
