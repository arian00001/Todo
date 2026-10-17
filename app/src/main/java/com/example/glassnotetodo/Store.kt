package com.example.glassnotetodo

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object Store {
    private const val PREFS = "glassnote_store"
    private const val NOTES = "notes"
    private const val TODOS = "todos"

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun notes(context: Context): List<Note> = runCatching {
        val array = JSONArray(prefs(context).getString(NOTES, "[]"))
        (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            Note(o.getLong("id"), o.optString("title"), o.optString("body"), o.optLong("updatedAt"))
        }.sortedByDescending { it.updatedAt }
    }.getOrDefault(emptyList())

    fun saveNotes(context: Context, notes: List<Note>) {
        val array = JSONArray()
        notes.forEach { n ->
            array.put(JSONObject().put("id", n.id).put("title", n.title).put("body", n.body).put("updatedAt", n.updatedAt))
        }
        prefs(context).edit().putString(NOTES, array.toString()).apply()
    }

    fun todos(context: Context): List<Todo> = runCatching {
        val array = JSONArray(prefs(context).getString(TODOS, "[]"))
        (0 until array.length()).map { i ->
            val o = array.getJSONObject(i)
            Todo(o.getLong("id"), o.optString("text"), o.optBoolean("done"), o.optLong("createdAt"))
        }.sortedWith(compareBy<Todo> { it.done }.thenByDescending { it.createdAt })
    }.getOrDefault(emptyList())

    fun saveTodos(context: Context, todos: List<Todo>) {
        val array = JSONArray()
        todos.forEach { t ->
            array.put(JSONObject().put("id", t.id).put("text", t.text).put("done", t.done).put("createdAt", t.createdAt))
        }
        prefs(context).edit().putString(TODOS, array.toString()).apply()
        TodoWidgetReceiver.refresh(context)
    }
}
