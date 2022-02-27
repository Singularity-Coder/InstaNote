package com.task.noteapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.task.noteapp.helpers.FragmentsTags
import com.task.noteapp.helpers.showScreen
import com.task.noteapp.notes.view.NotesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showScreen(NotesFragment(), FragmentsTags.NOTES.value, isAdd = true)
    }
}