package com.task.noteapp.notes.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.task.noteapp.MainActivity
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentNotesBinding
import com.task.noteapp.editnote.AddEditNoteFragment
import com.task.noteapp.helpers.*
import com.task.noteapp.notes.model.Note
import com.task.noteapp.notes.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment : Fragment(R.layout.fragment_notes) {

    private lateinit var binding: FragmentNotesBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var nnContext: Context
    private lateinit var nnActivity: MainActivity

    @Inject
    lateinit var notesAdapter: NotesAdapter

    /** Non Null Context and Activity */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        nnContext = context
        nnActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(nnActivity).get(NotesViewModel::class.java)
        binding = FragmentNotesBinding.bind(view)
        setUpRecyclerView()
        subscribeToObservers()
        setUpUserActionListeners()
    }

    private fun setUpRecyclerView() {
        binding.rvNotes.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun subscribeToObservers() {
        viewModel.noteList.observe(viewLifecycleOwner) { it: List<Note>? ->
            notesAdapter.noteList = it ?: emptyList()
            notesAdapter.notifyDataSetChanged()
            showEmptyListIndicator(it)
        }
    }

    private fun setUpUserActionListeners() {
        binding.fabAddNote.setOnClickListener {
            nnActivity.hideKeyboard()
            nnActivity.showScreen(AddEditNoteFragment(), FragmentsTags.ADD_EDIT.value, isAdd = true)
        }

        binding.rvNotes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.fabAddNote.hide()
                    binding.etSearchNotes.editText?.clearFocus()
                } else binding.fabAddNote.show()
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.refreshNotes.setOnRefreshListener {
            notesAdapter.noteList = viewModel.noteList.value ?: emptyList()
            notesAdapter.notifyDataSetChanged()
            showEmptyListIndicator(viewModel.noteList.value)
            binding.refreshNotes.isRefreshing = false
        }

        notesAdapter.setOnNoteLongPressListener { position: Int ->
            showOptionsPopupOnLongPress(position)
        }

        binding.etSearchNotes.editText?.addTextChangedListener { it: Editable? ->
            if (it.isNullOrBlank()) {
                notesAdapter.noteList = viewModel.noteList.value ?: emptyList()
                return@addTextChangedListener
            }
            notesAdapter.noteList = viewModel.noteList.value?.filter { note: Note ->
                note.title.toLowCase().contains(it.toString().toLowCase())
            } ?: emptyList()
        }
    }

    private fun showOptionsPopupOnLongPress(position: Int) {
        val optionsArray = arrayOf(getString(R.string.update_note), getString(R.string.delete_note))
        AlertDialog.Builder(nnContext).apply {
            setItems(optionsArray) { dialog, which ->
                when (which) {
                    0 -> {
                        val note = notesAdapter.noteList.getOrNull(position) ?: return@setItems
                        val bundle = Bundle().apply {
                            putParcelableArrayList(IntentKeys.KEY_UPDATE_NOTE.value, ArrayList<Note>().apply { add(note) })
                        }
                        val fragment = AddEditNoteFragment().apply { arguments = bundle }
                        nnActivity.showScreen(fragment, FragmentsTags.ADD_EDIT.value, isAdd = true)
                    }
                    1 -> {
                        val selectedArt = notesAdapter.noteList[position]
                        viewModel.deleteNote(selectedArt)
                    }
                }
            }
            create()
        }.show()
    }

    private fun showEmptyListIndicator(it: List<Note>?) {
        if (it.isNullOrEmpty()) binding.lottieEmpty.visible() else binding.lottieEmpty.gone()
    }
}