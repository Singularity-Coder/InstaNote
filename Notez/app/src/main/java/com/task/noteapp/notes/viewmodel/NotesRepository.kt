package com.task.noteapp.notes.viewmodel

import androidx.lifecycle.LiveData
import com.task.noteapp.notes.model.Note
import com.task.noteapp.helpers.db.NoteDao
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