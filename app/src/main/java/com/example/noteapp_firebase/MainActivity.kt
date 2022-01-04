package com.example.noteapp_firebase

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp_firebase.Adapter.RecyclerViewAdapter
import com.example.noteapp_firebase.Model.Note
import com.example.noteapp_firebase.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val adapter = RecyclerViewAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        viewModel.getAllNote().observe(this, { notes ->
            adapter.updateRV(notes)
        })





        binding.addNoteButton.setOnClickListener {
            showAddNoteDialog()
        }

        adapter.onClickItem = { notes ->
            showActionNoteDialog(notes)
        }
    }

    private fun showActionNoteDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Action")
        builder.setPositiveButton("Delete"){_, _ ->
            // viewModel.deleteNote(note)
            showDeleteDialog(note)
        }
        builder.setNegativeButton("Update"){_, _ ->
            showUpdateDialog(note)
        }
        builder.setNeutralButton("Cancel"){_, _ ->
            Toast.makeText(this,"Cancel", Toast.LENGTH_SHORT).show()
        }
        builder.create().show()
    }

    private fun showDeleteDialog(note: Note) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure want to delete this note?")
        builder.setPositiveButton("Delete") { _, _ ->
            viewModel.deleteNote(note.id)
        }
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.show()
    }


    private fun showUpdateDialog(note: Note) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_update_note)
        dialog.setCancelable(true)
        val etNoteTitle: EditText = dialog.findViewById(R.id.etNoteTitle)
        val etNoteDescription: EditText = dialog.findViewById(R.id.etNoteDescription)
        etNoteTitle.setText(note.noteTitle)
        etNoteDescription.setText(note.noteDescription)
        dialog.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.updateButton).setOnClickListener {
            if (inputCheck(etNoteTitle.text.toString(), etNoteDescription.text.toString())){
             viewModel.updateNote(note.id, etNoteTitle.text.toString(),etNoteDescription.text.toString())
                dialog.dismiss()
                Toast.makeText(this,"Note Updated", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Please enter data ", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.create()
        dialog.show()

    }


    private fun showAddNoteDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_new_note)
        dialog.setCancelable(true)
        val etNoteTitle: EditText = dialog.findViewById(R.id.etNoteTitle)
        val etNoteDescription: EditText = dialog.findViewById(R.id.etNoteDescription)
        dialog.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<Button>(R.id.addButton).setOnClickListener {
            if (inputCheck(etNoteTitle.text.toString(), etNoteDescription.text.toString())){
                val notes = Note("",etNoteTitle.text.toString(),etNoteDescription.text.toString())
                viewModel.addNote(notes)
                dialog.dismiss()
                Toast.makeText(this,"Data Added", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Please enter data ", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.create()
        dialog.show()

    }
    private fun inputCheck(noteTitle: String, noteDescription: String): Boolean {
        return !(TextUtils.isEmpty(noteTitle) && TextUtils.isEmpty(noteDescription))
    }
}