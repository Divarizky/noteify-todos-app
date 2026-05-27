package com.divarizky.noteify.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.divarizky.noteify.databinding.FragmentNoteBinding
import com.divarizky.noteify.data.local.Note

class NoteFragment(
    private val note: Note?,
    private val onSave: (String, String, Boolean) -> Unit
) : DialogFragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.90).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDialogTitle.text = if (note != null) "Edit Tugas" else "Tugas Baru"

        note?.let {
            binding.etTitle.setText(it.title)
            binding.etDescription.setText(it.description)
            binding.cbCompleteForm.isChecked = it.completed
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val description = binding.etDescription.text.toString().trim()
            val completed = binding.cbCompleteForm.isChecked
            if (title.isNotEmpty()) {
                onSave(title, description, completed)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}