package com.divarizky.noteify.view

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.divarizky.noteify.model.Note
import com.divarizky.noteify.databinding.NoteBinding

class NoteAdapter(
    private var notes: List<Note> = emptyList(),
    private val onEditClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit,
    private val onCheckedChange: (Note, Boolean) -> Unit
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: NoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.tvTitle.text = note.title

            // Fungsi untuk efek coret + redup jika completed
            applyStrikeThrough(note.completed)

            binding.cbComplete.setOnCheckedChangeListener(null)
            binding.cbComplete.isChecked = note.completed
            binding.cbComplete.setOnCheckedChangeListener { _, isChecked ->
                applyStrikeThrough(isChecked)
                onCheckedChange(note, isChecked)
            }

            binding.icEdit.setOnClickListener { onEditClick(note) }
            binding.icDelete.setOnClickListener { onDeleteClick(note) }
        }

        private fun applyStrikeThrough(isDone: Boolean) {
            if (isDone) {
                binding.tvTitle.paintFlags =
                    binding.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                binding.tvTitle.alpha = 0.45f
            } else {
                binding.tvTitle.paintFlags =
                    binding.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                binding.tvTitle.alpha = 1.0f
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun updateData(newNotes: List<Note>) {
        notes = newNotes
        notifyDataSetChanged()
    }
}