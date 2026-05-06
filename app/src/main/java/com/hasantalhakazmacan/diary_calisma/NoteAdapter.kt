package com.hasantalhakazmacan.diary_calisma

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: List<Note>,
    private val onClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvItemDate)
        val tvTitle: TextView = v.findViewById(R.id.tvItemTitle)
        val tvPreview: TextView = v.findViewById(R.id.tvItemPreview)
        val ivFav: ImageView = v.findViewById(R.id.ivItemFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val n = notes[pos]
        h.tvDate.text = n.date
        h.tvTitle.text = n.title
        h.tvPreview.text = n.content
        h.ivFav.visibility = if (n.isFavorite) View.VISIBLE else View.GONE
        h.itemView.setOnClickListener { onClick(n) }
    }

    override fun getItemCount() = notes.size

    fun update(newList: List<Note>) {
        notes = newList
        notifyDataSetChanged()
    }
}