package com.jimbonlemu.mynoteapps.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.jimbonlemu.mynoteapps.database.Note
import com.jimbonlemu.mynoteapps.database.NoteDao
import com.jimbonlemu.mynoteapps.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val mNotesDao: NoteDao
    private val executorService:ExecutorService = Executors.newSingleThreadExecutor()

    init {
        mNotesDao = NoteRoomDatabase.getDatabase(application).noteDao()
    }

    fun getAllNotes():LiveData<List<Note>> = mNotesDao.getAllNotes()

    fun insert(note: Note){
        executorService.execute{mNotesDao.insert(note)}
    }

    fun delete(note: Note){
        executorService.execute{mNotesDao.delete(note)}
    }

    fun update(note: Note){
        executorService.execute{mNotesDao.update(note)}
    }
}