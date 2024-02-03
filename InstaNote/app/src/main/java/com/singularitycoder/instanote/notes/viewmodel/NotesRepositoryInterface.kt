package com.singularitycoder.instanote.notes.viewmodel

import androidx.lifecycle.LiveData
import com.singularitycoder.instanote.notes.model.Note

interface NotesRepositoryInterface {
    fun getNotes(): LiveData<List<Note>>
    suspend fun insertNote(note: Note) = Unit
    suspend fun updateNote(note: Note) = Unit
    suspend fun deleteNote(note: Note) = Unit
}