package com.divarizky.noteify.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.divarizky.noteify.databinding.ActivityMainBinding
import com.divarizky.noteify.model.Note
import com.divarizky.noteify.viewmodel.NoteViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeViewModel()
        setupListeners()
        viewModel.loadNotes()
    }

    private fun setupRecyclerView() {
        adapter = NoteAdapter(
            onEditClick     = { note -> showNoteFragment(note) },
            onDeleteClick   = { note -> viewModel.deleteNote(note) },
            onCheckedChange = { note, isChecked -> viewModel.toggleComplete(note, isChecked) }
        )
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun observeViewModel() {
        viewModel.notes.observe(this) { items ->
            adapter.updateData(items)
            binding.layoutEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.errorMessage.observe(this) { message ->
            message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }
    }

    private fun setupListeners() {
        binding.btnAddNote.setOnClickListener { showNoteFragment(null) }
    }

    private fun showNoteFragment(note: Note?) {
        val fragment = NoteFragment(note) { title, description, completed ->
            if (note == null) viewModel.addNote(title, description, completed)
            else viewModel.updateNote(note.copy(title = title, description = description, completed = completed))
        }
        fragment.show(supportFragmentManager, "NoteFragment")
    }
}