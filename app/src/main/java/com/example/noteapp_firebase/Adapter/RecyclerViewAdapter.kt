package com.example.noteapp_firebase.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_firebase.Model.Note
import com.example.noteapp_firebase.databinding.ItemRowBinding

class RecyclerViewAdapter(): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>() {
    private var notes = emptyList<Note>()
    var onClickItem:((Note) -> Unit)? = null
    class ItemViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            ItemRowBinding.inflate(
                LayoutInflater.from(parent.context), parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.apply {
            tvNoteTitle.text = note.noteTitle
            tvNoteDescription.text = note.noteDescription

        }
        holder.itemView.setOnClickListener {
            onClickItem?.invoke(notes[position])
        }
    }
    override fun getItemCount(): Int {
        return notes.size
    }

    fun updateRV(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }
}