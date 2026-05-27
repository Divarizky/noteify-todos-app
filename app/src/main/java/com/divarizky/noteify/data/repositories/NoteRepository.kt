package com.divarizky.noteify.data.repositories

import com.divarizky.noteify.data.local.Note
import com.divarizky.noteify.data.local.NoteDao
import com.divarizky.noteify.services.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NoteRepository(
    private val noteDao: NoteDao,
    private val apiService: ApiService
) {

    suspend fun fetchNotes(): List<Note> = withContext(Dispatchers.IO) {
        val localNotes = noteDao.getAllNotes()
        if (localNotes.isEmpty()) {
            syncFromApi()
        }
        noteDao.getAllNotes()
    }

    private suspend fun syncFromApi() {
        try {
            val apiNotes = apiService.getNotes()
            android.util.Log.d("SyncAPI", "Fetched ${apiNotes.size} notes from API")
            apiNotes.forEach { apiNote ->
                android.util.Log.d("SyncAPI", "Note: ${apiNote.title}, completed: ${apiNote.completed}")
            }
            val roomNotes = apiNotes.map { apiNote ->
                Note(
                    title = apiNote.title,
                    description = "",
                    completed = apiNote.completed
                )
            }
            roomNotes.forEach { noteDao.upsertNotes(it) }
        } catch (e: Exception) {
            android.util.Log.e("SyncAPI", "Gagal sinkronisasi API: ${e.message}", e)
        }
    }

    suspend fun saveNotes(note: Note) = withContext(Dispatchers.IO) {
        noteDao.upsertNotes(note)
    }

    suspend fun deleteNotes(note: Note) = withContext(Dispatchers.IO) {
        noteDao.deleteNotes(note)
    }
}