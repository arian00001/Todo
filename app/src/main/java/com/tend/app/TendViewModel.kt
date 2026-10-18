package com.tend.app

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tend.app.data.Note
import com.tend.app.data.Priority
import com.tend.app.data.Store
import com.tend.app.data.Task
import com.tend.app.data.TaskStatus
import com.tend.app.widget.WidgetRefresh
import kotlinx.coroutines.flow.StateFlow

class TendViewModel(private val app: Application) : AndroidViewModel(app) {
    val tasks: StateFlow<List<Task>> = Store.tasks
    val notes: StateFlow<List<Note>> = Store.notes

    fun addTask(title: String, notes: String, priority: Priority, dueDate: String) {
        Store.addTask(title, notes, priority, dueDate)
        WidgetRefresh.request(app)
    }

    fun updateTask(task: Task) {
        Store.updateTask(task)
        WidgetRefresh.request(app)
    }

    fun setStatus(id: String, status: TaskStatus) {
        Store.setStatus(id, status)
        WidgetRefresh.request(app)
    }

    fun toggleDone(id: String) {
        Store.toggleDone(id)
        WidgetRefresh.request(app)
    }

    fun deleteTask(id: String) {
        Store.deleteTask(id)
        WidgetRefresh.request(app)
    }

    fun newNote(): Note {
        val note = Store.newNote()
        Store.upsertNote(note)
        return note
    }

    fun saveNote(note: Note) {
        Store.upsertNote(note)
    }

    fun togglePinned(id: String) {
        Store.togglePinned(id)
    }

    fun deleteNote(id: String) {
        Store.deleteNote(id)
    }
}
