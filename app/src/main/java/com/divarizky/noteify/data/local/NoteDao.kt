package com.divarizky.noteify.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes")
    fun getAllNotes(): List<Note>

    @Upsert
    suspend fun upsertNotes(note: Note): Long

    @Delete
    suspend fun deleteNotes(note: Note): Int
}