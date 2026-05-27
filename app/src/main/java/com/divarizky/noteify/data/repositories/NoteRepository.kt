package com.divarizky.noteify.data.repositories

import com.divarizky.noteify.data.local.Note
import com.divarizky.noteify.data.local.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository (private val noteDao: NoteDao) {

    suspend fun fetchNotes(): List<Note> = withContext(Dispatchers.IO) {
        noteDao.getAllNotes()
    }

    suspend fun saveNotes(note: Note) = withContext(Dispatchers.IO) {
        noteDao.upsertNotes(note)
    }

    suspend fun deleteNotes(note: Note) = withContext(Dispatchers.IO) {
        noteDao.deleteNotes(note)
    }
}