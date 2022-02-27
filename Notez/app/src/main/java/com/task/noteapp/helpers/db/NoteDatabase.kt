package com.task.noteapp.helpers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.task.noteapp.notes.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
