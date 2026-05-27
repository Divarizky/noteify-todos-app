package com.divarizky.noteify.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.divarizky.noteify.data.local.Note
import com.divarizky.noteify.data.local.NotesDatabase
import com.divarizky.noteify.data.repositories.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NoteRepository

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // Inisialisasi NotesDatabase
    init {
        val noteDao = NotesDatabase.getDatabase(application).noteDao()
        repository = NoteRepository(noteDao)
    }

    fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _notes.value = repository.fetchNotes()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Gagal memuat data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addNote(title: String, description: String, completed: Boolean) {
        viewModelScope.launch {
            val newNote = Note(title = title, description = description, completed = completed)
            repository.saveNotes(newNote)
            loadNotes()
        }
    }

    fun updateNote(updatedNote: Note) {
        viewModelScope.launch {
            repository.saveNotes(updatedNote)
            loadNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNotes(note)
            loadNotes()
        }
    }

    fun toggleComplete(note: Note, isChecked: Boolean) {
        viewModelScope.launch {
            repository.saveNotes(note.copy(completed = isChecked))
            loadNotes()
        }
    }
}