package com.singularitycoder.instanote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.singularitycoder.instanote.helpers.FragmentsTags
import com.singularitycoder.instanote.helpers.showScreen
import com.singularitycoder.instanote.notes.view.NotesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showScreen(NotesFragment(), FragmentsTags.NOTES.value, isAdd = true)
    }
}