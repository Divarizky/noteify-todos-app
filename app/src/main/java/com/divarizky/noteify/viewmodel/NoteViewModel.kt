package com.divarizky.noteify.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.divarizky.noteify.model.Note
import com.divarizky.noteify.repositories.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel : ViewModel() {

    private val repository = NoteRepository()

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = repository.fetchNotes()
                _notes.value = result
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Gagal memuat data"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addNote(title: String, completed: Boolean) {
        val currentList = _notes.value?.toMutableList() ?: mutableListOf()
        val newId = (currentList.maxOfOrNull { it.id } ?: 0) + 1
        currentList.add(0, Note(userId = 1, id = newId, title = title, completed = completed))
        _notes.value = currentList
    }

    fun updateNote(updatedNote: Note) {
        val currentList = _notes.value?.toMutableList() ?: return
        val index = currentList.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            currentList[index] = updatedNote
            _notes.value = currentList
        }
    }

    fun deleteNote(note: Note) {
        val currentList = _notes.value?.toMutableList() ?: return
        currentList.remove(note)
        _notes.value = currentList
    }

    fun toggleComplete(note: Note, isChecked: Boolean) {
        updateNote(note.copy(completed = isChecked))
    }
}