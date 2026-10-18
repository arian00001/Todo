package com.tend.app.data

import org.json.JSONObject

enum class TaskStatus(val label: String) {
    TODO("To do"),
    IN_PROGRESS("In progress"),
    DONE("Done")
}

enum class Priority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

data class Task(
    val id: String,
    val title: String,
    val notes: String = "",
    val status: TaskStatus = TaskStatus.TODO,
    val priority: Priority = Priority.MEDIUM,
    val dueDate: String = "",
    val createdAt: Long = System.currentTimeMillis()
) {
    val isDone: Boolean get() = status == TaskStatus.DONE

    fun toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("title", title)
        .put("notes", notes)
        .put("status", status.name)
        .put("priority", priority.name)
        .put("dueDate", dueDate)
        .put("createdAt", createdAt)

    companion object {
        fun fromJson(json: JSONObject): Task = Task(
            id = json.optString("id"),
            title = json.optString("title"),
            notes = json.optString("notes"),
            status = runCatching { TaskStatus.valueOf(json.optString("status", "TODO")) }.getOrDefault(TaskStatus.TODO),
            priority = runCatching { Priority.valueOf(json.optString("priority", "MEDIUM")) }.getOrDefault(Priority.MEDIUM),
            dueDate = json.optString("dueDate"),
            createdAt = json.optLong("createdAt", System.currentTimeMillis())
        )
    }
}

data class Note(
    val id: String,
    val title: String,
    val body: String = "",
    val pinned: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toJson(): JSONObject = JSONObject()
        .put("id", id)
        .put("title", title)
        .put("body", body)
        .put("pinned", pinned)
        .put("updatedAt", updatedAt)

    companion object {
        fun fromJson(json: JSONObject): Note = Note(
            id = json.optString("id"),
            title = json.optString("title"),
            body = json.optString("body"),
            pinned = json.optBoolean("pinned", false),
            updatedAt = json.optLong("updatedAt", System.currentTimeMillis())
        )
    }
}
