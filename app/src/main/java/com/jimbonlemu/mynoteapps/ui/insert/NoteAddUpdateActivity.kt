package com.jimbonlemu.mynoteapps.ui.insert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.R.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.jimbonlemu.mynoteapps.R
import com.jimbonlemu.mynoteapps.database.Note
import com.jimbonlemu.mynoteapps.databinding.ActivityNoteAddUpdateBinding
import com.jimbonlemu.mynoteapps.helper.DateHelper
import com.jimbonlemu.mynoteapps.helper.ViewModelFactory

@Suppress("DEPRECATION")
class NoteAddUpdateActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_NOTE = "extra_note"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 10
    }

    private var isEdit = false
    private var note: Note? = null
    private lateinit var noteAddUpdateViewModel: NoteAddUpdateViewModel
    private var _activityNoteAddUpdateBinding: ActivityNoteAddUpdateBinding? = null
    private val binding get() = _activityNoteAddUpdateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityNoteAddUpdateBinding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        note = intent.getParcelableExtra<Note>(EXTRA_NOTE)

        if (note != null) {
            isEdit = true
        } else {
            note = Note()
        }

        var actionBarTitle: String
        var btnTitle: String

        if (isEdit) {
            actionBarTitle = getString(R.string.change)
            btnTitle = getString(R.string.update)
            if (note != null) {
                note?.let { note ->
                    binding?.editTitle?.setText(note.title)
                    binding?.edtDescription?.setText(note.description)
                }
            } else {
                actionBarTitle = getString(R.string.add)
                btnTitle = getString(R.string.save)
            }

            supportActionBar?.title = actionBarTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            binding?.btnSubmit?.text = btnTitle
            binding?.btnSubmit?.setOnClickListener {
                val title = binding?.editTitle?.text.toString().trim()
                val description = binding?.edtDescription?.text.toString().trim()
                when {
                    title.isEmpty() -> {
                        binding?.editTitle?.error = getString(R.string.empty)
                    }

                    description.isEmpty() -> {
                        binding?.edtDescription?.error = getString(R.string.empty)
                    }

                    else -> {
                        note.let { note ->
                            note?.title = title
                            note?.description = description
                        }

                        if (isEdit) {
                            noteAddUpdateViewModel.update(note as Note)
                            showToast(getString(R.string.changed))
                        } else {
                            note.let { note ->
                                note?.date = DateHelper.getCurrentDate()
                            }
                            noteAddUpdateViewModel.insert(note as Note)
                            showToast(getString(R.string.added))
                        }
                        finish()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        showAlertDialog(ALERT_DIALOG_CLOSE)
        return super.getOnBackInvokedDispatcher()
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityNoteAddUpdateBinding = null
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel)
            dialogMessage = getString(R.string.message_cancel)
        } else {
            dialogMessage = getString(R.string.message_delete)
            dialogTitle = getString(R.string.delete)
        }

        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                if (!isDialogClose) {
                    noteAddUpdateViewModel.delete(note as Note)
                    showToast(getString(R.string.deleted))
                }
                finish()
            }
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.cancel()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NoteAddUpdateViewModel::class.java]
    }
}
