package com.divarizky.noteify.repositories

import com.divarizky.noteify.model.Note
import com.divarizky.noteify.services.RetrofitClient

class NoteRepository {
    suspend fun fetchNotes(): List<Note> {
        return RetrofitClient.instance.getNotes()
    }
}