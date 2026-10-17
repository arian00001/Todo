package com.example.glassnotetodo

data class Note(val id: Long, val title: String, val body: String, val updatedAt: Long)
data class Todo(val id: Long, val text: String, val done: Boolean, val createdAt: Long)
