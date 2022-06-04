package com.task.noteapp.helpers

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.task.noteapp.R
import com.task.noteapp.MainActivity
import com.task.noteapp.editnote.AddEditNoteFragment
import com.task.noteapp.notes.view.NotesFragment
import com.task.noteapp.imageselection.view.ImageSelectionFragment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Db {
    const val DB_NOTES = "db_notes"
    const val TABLE_NOTES = "table_notes"
}

enum class FragmentsTags(val value: String) {
    NOTES(NotesFragment::class.java.simpleName),
    ADD_EDIT(AddEditNoteFragment::class.java.simpleName),
    IMAGE_SELECTION(ImageSelectionFragment::class.java.simpleName)
}

enum class IntentKeys(val value: String) {
    KEY_UPDATE_NOTE("KEY_UPDATE_NOTE")
}

val timeNow: Long
    get() = System.currentTimeMillis()

fun Long.toIntuitiveDateTime(): String {
    val postedTime = this
    val elapsedTimeMillis = timeNow - postedTime
    val elapsedTimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTimeMillis)
    val elapsedTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTimeMillis)
    val elapsedTimeInHours = TimeUnit.MILLISECONDS.toHours(elapsedTimeMillis)
    val elapsedTimeInDays = TimeUnit.MILLISECONDS.toDays(elapsedTimeMillis)
    val elapsedTimeInMonths = elapsedTimeInDays / 30
    return when {
        elapsedTimeInSeconds < 60 -> "Now"
        elapsedTimeInMinutes == 1L -> "$elapsedTimeInMinutes Minute ago"
        elapsedTimeInMinutes < 60 -> "$elapsedTimeInMinutes Minutes ago"
        elapsedTimeInHours == 1L -> "$elapsedTimeInHours Hour ago"
        elapsedTimeInHours < 24 -> "$elapsedTimeInHours Hours ago"
        elapsedTimeInDays == 1L -> "$elapsedTimeInDays Day ago"
        elapsedTimeInDays < 30 -> "$elapsedTimeInDays Days ago"
        elapsedTimeInMonths == 1L -> "$elapsedTimeInMonths Month ago"
        elapsedTimeInMonths < 12 -> "$elapsedTimeInMonths Months ago"
        else -> postedTime toTimeOfType "dd MMM yyyy, hh:mm a"
    }
}

infix fun Long.toTimeOfType(type: String): String {
    val date = Date(this)
    val dateFormat = SimpleDateFormat(type, Locale.getDefault())
    return dateFormat.format(date)
}

fun MainActivity.showScreen(
    fragment: Fragment,
    tag: String,
    isAdd: Boolean = false
) {
    if (isAdd) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_to_left, R.anim.slide_to_right, R.anim.slide_to_left, R.anim.slide_to_right)
            .add(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    } else {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .commit()
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun String.toLowCase(): String = this.lowercase(Locale.getDefault())

fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    // Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    // If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context?.clipboard(): ClipboardManager? = this?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

fun EditText.disableCopyPaste() {
    isLongClickable = false
    setTextIsSelectable(false)
    customSelectionActionModeCallback = object : android.view.ActionMode.Callback {
        override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean = false
        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean = false
        override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean = false
        override fun onDestroyActionMode(p0: ActionMode?) = Unit
    }
}