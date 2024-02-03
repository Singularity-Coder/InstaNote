package com.singularitycoder.instanote.helpers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.singularitycoder.instanote.helpers.db.NoteDao
import com.singularitycoder.instanote.notes.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}
