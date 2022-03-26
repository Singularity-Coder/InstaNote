package com.task.noteapp.notes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.task.noteapp.notes.model.Note
import com.task.noteapp.notes.viewmodel.NotesRepositoryInterface

class FakeNotesRepository : NotesRepositoryInterface {

    private val notes = mutableListOf<Note>()
    private val notesLiveData = MutableLiveData<List<Note>>(notes)

    override fun getNotes(): LiveData<List<Note>> {
        return notesLiveData
    }

    override suspend fun insertNote(note: Note) {
        notes.add(note)
        notesLiveData.postValue(notes)
    }

    override suspend fun updateNote(note: Note) {
        notes[0] = note
        notesLiveData.postValue(notes)
    }

    override suspend fun deleteNote(note: Note) {
        notes.remove(note)
        notesLiveData.postValue(notes)
    }
}