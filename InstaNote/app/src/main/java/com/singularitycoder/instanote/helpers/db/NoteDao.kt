package com.singularitycoder.instanote.helpers.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.singularitycoder.instanote.helpers.Db
import com.singularitycoder.instanote.notes.model.Note

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