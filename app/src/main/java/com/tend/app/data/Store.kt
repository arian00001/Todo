package com.tend.app.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

object Store {
    private const val PREFS_NAME = "tend_store"
    private const val KEY_TASKS = "tasks"
    private const val KEY_NOTES = "notes"

    private var prefs: SharedPreferences? = null

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    fun init(context: Context) {
        if (prefs != null) return
        val store = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs = store
        _tasks.value = readTasks(store)
        _notes.value = readNotes(store)
    }

    fun addTask(title: String, notes: String, priority: Priority, dueDate: String) {
        val task = Task(
            id = UUID.randomUUID().toString(),
            title = title,
            notes = notes,
            priority = priority,
            dueDate = dueDate
        )
        _tasks.value = listOf(task) + _tasks.value
        persistTasks()
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
        persistTasks()
    }

    fun setStatus(id: String, status: TaskStatus) {
        _tasks.value = _tasks.value.map { if (it.id == id) it.copy(status = status) else it }
        persistTasks()
    }

    fun toggleDone(id: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == id) it.copy(status = if (it.isDone) TaskStatus.TODO else TaskStatus.DONE) else it
        }
        persistTasks()
    }

    fun deleteTask(id: String) {
        _tasks.value = _tasks.value.filterNot { it.id == id }
        persistTasks()
    }

    fun newNote(): Note = Note(id = UUID.randomUUID().toString(), title = "", body = "")

    fun upsertNote(note: Note) {
        val stamped = note.copy(updatedAt = System.currentTimeMillis())
        val known = _notes.value.any { it.id == note.id }
        _notes.value = if (known) {
            _notes.value.map { if (it.id == note.id) stamped else it }
        } else {
            listOf(stamped) + _notes.value
        }
        persistNotes()
    }

    fun togglePinned(id: String) {
        _notes.value = _notes.value.map { if (it.id == id) it.copy(pinned = !it.pinned) else it }
        persistNotes()
    }

    fun deleteNote(id: String) {
        _notes.value = _notes.value.filterNot { it.id == id }
        persistNotes()
    }

    fun openTaskCount(): Int = _tasks.value.count { !it.isDone }

    fun openTaskPreview(limit: Int): List<String> =
        _tasks.value.filter { !it.isDone }.take(limit).map { it.title }

    private fun persistTasks() {
        prefs?.edit()?.putString(KEY_TASKS, encode(_tasks.value.map { it.toJson() }))?.apply()
    }

    private fun persistNotes() {
        prefs?.edit()?.putString(KEY_NOTES, encode(_notes.value.map { it.toJson() }))?.apply()
    }

    private fun encode(items: List<JSONObject>): String {
        val array = JSONArray()
        items.forEach { array.put(it) }
        return array.toString()
    }

    private fun readTasks(store: SharedPreferences): List<Task> {
        val raw = store.getString(KEY_TASKS, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).map { Task.fromJson(array.getJSONObject(it)) }
        }.getOrDefault(emptyList())
    }

    private fun readNotes(store: SharedPreferences): List<Note> {
        val raw = store.getString(KEY_NOTES, null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).map { Note.fromJson(array.getJSONObject(it)) }
        }.getOrDefault(emptyList())
    }
}
