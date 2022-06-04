package com.task.noteapp.editnote

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.RequestManager
import com.task.noteapp.MainActivity
import com.task.noteapp.R
import com.task.noteapp.databinding.FragmentAddEditNoteBinding
import com.task.noteapp.helpers.*
import com.task.noteapp.imageselection.view.ImageSelectionAdapter
import com.task.noteapp.imageselection.view.ImageSelectionFragment
import com.task.noteapp.imageselection.viewmodel.ImageSelectionViewModel
import com.task.noteapp.notes.model.Note
import com.task.noteapp.notes.viewmodel.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddEditNoteFragment : Fragment(R.layout.fragment_add_edit_note) {

    private lateinit var nnContext: Context
    private lateinit var nnActivity: MainActivity
    private lateinit var binding: FragmentAddEditNoteBinding

    private val notesViewModel: NotesViewModel by viewModels()
    private val imageSelectionViewModel: ImageSelectionViewModel by activityViewModels()

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var imageSelectionAdapter: ImageSelectionAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        nnContext = context
        nnActivity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddEditNoteBinding.bind(view)
        getNoteBundleData()
        subscribeToObservers()
        setUpUserActionListeners()
    }

    override fun onResume() {
        super.onResume()
        nnContext.clipboard()?.text = ""
        binding.etDescription.editText?.disableCopyPaste()
    }

    private fun getNoteBundleData() {
        val bundle = arguments ?: return
        val note = bundle.getParcelableArrayList<Note>(IntentKeys.KEY_UPDATE_NOTE.value)?.firstOrNull() ?: return

        binding.apply {
            etTitle.editText?.setText(note.title)
            etDescription.editText?.setText(note.description)
            glide.load(note.imageUrl).into(ivNoteImage)
            imageSelectionViewModel.setSelectedImage(note.imageUrl)
        }
    }

    private fun subscribeToObservers() {
        imageSelectionViewModel.selectedImageUrl.observe(viewLifecycleOwner) { it: String? ->
            it ?: return@observe
            glide.load(it).into(binding.ivNoteImage)
        }
    }

    private fun setUpUserActionListeners() {
        binding.etDescription.editText?.doOnTextChanged { text, _, _, _ ->
            text ?: return@doOnTextChanged
            // If first char is a new line or a space then reset text
            if (text.startsWith(prefix = "\n", ignoreCase = true) || text.startsWith(prefix = " ", ignoreCase = true)) {
                binding.etDescription.editText?.setText("")
            }
            binding.btnSaveNote.isEnabled = text.isNotBlank()
            // TODO check if the existing text is same as new text and disable save button while updating notes.
        }

        binding.ivNoteImage.setOnClickListener {
            nnActivity.showScreen(ImageSelectionFragment(), FragmentsTags.IMAGE_SELECTION.value, isAdd = true)
        }

        nnActivity.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = jumpBack()
        })

        binding.btnSaveNote.setOnClickListener {
            binding.etTitle.error = null
            binding.etDescription.error = null

            val oldNote = arguments?.getParcelableArrayList<Note>(IntentKeys.KEY_UPDATE_NOTE.value)?.firstOrNull()

            val title = binding.etTitle.editText?.text.toString()
            val description = binding.etDescription.editText?.text.toString()

            if (title.isBlank()) {
                binding.etTitle.error = getString(R.string.title_is_necessary)
                return@setOnClickListener
            }

            if (description.isBlank()) {
                binding.etDescription.error = getString(R.string.desc_is_necessary)
                return@setOnClickListener
            }

            if (description.length > 150) {
                binding.etDescription.error = getString(R.string.max_150_characters)
                return@setOnClickListener
            }

            val newNote = Note(
                id = oldNote?.id,
                title = title,
                description = description,
                date = if (null == arguments) timeNow else oldNote?.date ?: timeNow,
                imageUrl = imageSelectionViewModel.selectedImageUrl.value,
                isEdited = false
            )

            if (null != oldNote && (oldNote.title != newNote.title ||
                        oldNote.description != newNote.description ||
                        oldNote.imageUrl != newNote.imageUrl)
            ) newNote.isEdited = true

            if (null == arguments) notesViewModel.insertNote(newNote) else {
                if (newNote.isEdited) notesViewModel.updateNote(newNote)
            }

            jumpBack()
        }

        binding.btnCancel.setOnClickListener { jumpBack() }
    }

    private fun jumpBack() {
        imageSelectionViewModel.setSelectedImage(null)
        nnActivity.supportFragmentManager.popBackStackImmediate()
    }
}