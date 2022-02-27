package com.task.noteapp.notes.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.task.noteapp.helpers.Db

@kotlinx.parcelize.Parcelize
@Entity(tableName = Db.TABLE_NOTES)
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    var title: String,
    var description: String,
    var date: Long,
    var imageUrl: String? = null,
    var isEdited: Boolean = false
): Parcelable
