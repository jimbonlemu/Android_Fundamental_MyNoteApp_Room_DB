package com.jimbonlemu.mynoteapps.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jimbonlemu.mynoteapps.database.Note
import com.jimbonlemu.mynoteapps.databinding.ItemNoteBinding
import com.jimbonlemu.mynoteapps.helper.NoteDiffCallback
import com.jimbonlemu.mynoteapps.ui.insert.NoteAddUpdateActivity

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private val listNotes = ArrayList<Note>()
    fun setListNotes(listNotes:List<Note>){
        val diffCallback = NoteDiffCallback(this.listNotes, listNotes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listNotes.clear()
        this.listNotes.addAll(listNotes)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
       val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun getItemCount(): Int = listNotes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) =  holder.bind(listNotes[position])

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) {
            with(binding) {
                tvItemTitle.text = note.title
                tvItemDate.text = note.date
                tvItemDescription.text = note.description
                cvItemNote.setOnClickListener {
                    with(it) {
                        context.startActivity(
                            Intent(
                                context,
                                NoteAddUpdateActivity::class.java
                            ).putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                        )
                    }

                }
            }
        }
    }
}
