package com.task.noteapp.helpers.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.task.noteapp.helpers.Db
import com.task.noteapp.notes.model.Note

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM ${Db.TABLE_NOTES}")
    fun observeNotes(): LiveData<List<Note>>
}