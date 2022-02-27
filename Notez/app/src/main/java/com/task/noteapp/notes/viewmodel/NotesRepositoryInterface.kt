package com.task.noteapp.notes.viewmodel

import androidx.lifecycle.LiveData
import com.task.noteapp.notes.model.Note

interface NotesRepositoryInterface {
    fun getNotes(): LiveData<List<Note>>
    suspend fun insertNote(note: Note) = Unit
    suspend fun updateNote(note: Note) = Unit
    suspend fun deleteNote(note: Note) = Unit
}