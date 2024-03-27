package com.jimbonlemu.mynoteapps.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jimbonlemu.mynoteapps.R
import com.jimbonlemu.mynoteapps.databinding.ActivityMainBinding
import com.jimbonlemu.mynoteapps.helper.ViewModelFactory
import com.jimbonlemu.mynoteapps.ui.insert.NoteAddUpdateActivity

class MainActivity : AppCompatActivity() {
    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding
    private lateinit var adapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val mainViewModel = obtainViewModel(this)
        mainViewModel.getAllNotes().observe(this) { noteList ->
            if (noteList != null) {
                adapter.setListNotes(noteList)
            }
        }

        adapter = NoteAdapter()
        initNoteAdapter(adapter)

        binding?.fabAdd?.setOnClickListener {
            startActivity(Intent(this, NoteAddUpdateActivity::class.java))
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    private fun initNoteAdapter(adapter: NoteAdapter) {
        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
        binding?.rvNotes?.setHasFixedSize(true)
        binding?.rvNotes?.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityMainBinding = null
    }
}