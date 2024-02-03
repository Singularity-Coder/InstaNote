package com.singularitycoder.instanote.notes.viewmodel

import androidx.lifecycle.LiveData
import com.singularitycoder.instanote.notes.model.Note
import com.singularitycoder.instanote.helpers.db.NoteDao
import javax.inject.Inject

class NotesRepository @Inject constructor(private val noteDao: NoteDao) : NotesRepositoryInterface {

    override fun getNotes(): LiveData<List<Note>> {
        return noteDao.observeNotes()
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}