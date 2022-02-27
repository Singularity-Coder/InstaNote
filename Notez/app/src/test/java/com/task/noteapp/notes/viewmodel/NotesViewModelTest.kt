package com.task.noteapp.notes.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.task.noteapp.MainCoroutineRule
import com.task.noteapp.notes.repository.FakeNotesRepository
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.getOrAwaitValueTest
import com.task.noteapp.helpers.timeNow
import com.task.noteapp.notes.model.Note
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: NotesViewModel
    private val note1 = Note(id = 1, title = "Do It", description = "Do Do Do Do", date = timeNow, imageUrl = "")

    @Before
    fun setup() {
        viewModel = NotesViewModel(FakeNotesRepository())
        viewModel.insertNote(note1)
    }

    @Test
    fun `insert note and confirm if the inserted item id is 12`() {
        viewModel.insertNote(Note(id = 12, title = "Do something", description = "Watch Anime", date = timeNow, imageUrl = ""))
        val noteList = viewModel.noteList.getOrAwaitValueTest()
        assertThat(noteList.last().id).isEqualTo(12)
    }

    @Test
    fun `update note and confirm if the updated note has title Do something useful`() {
        viewModel.updateNote(Note(id = 12, title = "Do something useful", description = "Watch Tutorial", date = timeNow, imageUrl = ""))
        val noteList = viewModel.noteList.getOrAwaitValueTest()
        assertThat(noteList[0].title).isEqualTo("Do something useful")
    }

    @Test
    fun `delete note and confirm if the note with id 1 is gone`() {
        viewModel.deleteNote(note1)
        val noteList = viewModel.noteList.getOrAwaitValueTest()
        assertFalse(noteList.contains(note1))
    }
}