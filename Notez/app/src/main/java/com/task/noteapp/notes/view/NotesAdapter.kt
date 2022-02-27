package com.task.noteapp.notes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.task.noteapp.databinding.ListItemNoteBinding
import com.task.noteapp.helpers.gone
import com.task.noteapp.helpers.toIntuitiveDateTime
import com.task.noteapp.helpers.toTimeOfType
import com.task.noteapp.helpers.visible
import com.task.noteapp.notes.model.Note
import javax.inject.Inject

class NotesAdapter @Inject constructor(val glide: RequestManager) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val diffUtil = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
    }
    private val noteListDiffer = AsyncListDiffer(this, diffUtil)
    private var onItemClickListener: ((position: Int) -> Unit)? = null
    var noteList: List<Note>
        get() = noteListDiffer.currentList
        set(value) = noteListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = ListItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = noteList[position]
        holder.setData(note)
    }

    override fun getItemCount(): Int = noteList.size

    override fun getItemViewType(position: Int): Int = position

    fun setOnNoteLongPressListener(listener: (position: Int) -> Unit) {
        onItemClickListener = listener
    }

    inner class NoteViewHolder(private val itemBinding: ListItemNoteBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun setData(note: Note) {
            itemBinding.apply {
                if (note.imageUrl.isNullOrBlank()) {
                    ivNoteImage.gone()
                } else {
                    ivNoteImage.visible()
                    glide.load(note.imageUrl).into(ivNoteImage)
                }
                tvTitle.text = note.title
                tvDescription.text = note.description
                tvDate.text = note.date.toIntuitiveDateTime()
                if (note.isEdited) tvEdited.visible() else tvEdited.gone()
                root.setOnLongClickListener {
                    onItemClickListener?.let { listener: (position: Int) -> Unit -> listener.invoke(adapterPosition) }
                    return@setOnLongClickListener false
                }
            }
        }
    }
}