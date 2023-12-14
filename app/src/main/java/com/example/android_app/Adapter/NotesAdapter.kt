package com.example.android_app.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_app.MainActivity
import com.example.android_app.Models.Note
import com.example.android_app.R
import kotlin.random.Random

class NotesAdapter(
    private val context: Context,
    private val listener: MainActivity
) : RecyclerView.Adapter<NotesAdapter.NoteViewsHolder>() {

    // Lista de notas exibidas no RecyclerView
    private val notesList = ArrayList<Note>()

    // Lista completa de todas as notas, usada para filtragem
    private val fullList = ArrayList<Note>()

    // Cria novas instâncias de NoteViewsHolder (chamado pelo RecyclerView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewsHolder {
        return NoteViewsHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        )
    }

    // Retorna o número de itens na lista de notas
    override fun getItemCount(): Int {
        return notesList.size
    }

    // Vincula os dados de uma nota à exibição correspondente no RecyclerView
    override fun onBindViewHolder(holder: NoteViewsHolder, position: Int) {
        val currentNote = notesList[position]

        // Configura os dados da nota nas exibições correspondentes
        holder.title.text = currentNote.title
        holder.title.isSelected = true

        holder.note_tv.text = currentNote.note
        holder.date.text = currentNote.date
        holder.date.isSelected = true

        // Define a cor de fundo do CardView aleatoriamente
        holder.notes_layout.setCardBackgroundColor(
            holder.itemView.resources.getColor(randomColor(), null)
        )

        // Configura o ouvinte de clique curto para o CardView
        holder.notes_layout.setOnClickListener {
            listener.onItemClick(notesList[holder.adapterPosition])
        }

        // Configura o ouvinte de clique longo para o CardView
        holder.notes_layout.setOnLongClickListener {
            listener.onLongItemClicked(
                notesList[holder.adapterPosition],
                holder.notes_layout
            )
            true
        }
    }

    // Atualiza a lista de notas exibidas no RecyclerView
    fun updateList(newList: List<Note>) {
        fullList.clear()
        fullList.addAll(newList)
        notifyDataSetChanged()
    }

    // Filtra a lista de notas com base no texto de pesquisa
    fun filterList(search: String) {
        notesList.clear()
        for (item in fullList) {
            if (item.title?.lowercase()?.contains(search.lowercase()) == true ||
                item.note?.lowercase()?.contains(search.lowercase()) == true
            ) {
                notesList.add(item)
            }
        }
        notifyDataSetChanged()
    }

    // Gera uma cor aleatória a partir de uma lista predefinida de cores
    private fun randomColor(): Int {
        val list = listOf(
            R.color.rosa_claro,
            R.color.rosa,
            R.color.rosa_escuro
        )

        // Seleciona uma cor aleatória da lista usando um índice gerado aleatoriamente
        val seed = System.currentTimeMillis().toInt()
        val randomIndex = Random(seed).nextInt(list.size)
        return list[randomIndex]
    }

    // Representa as exibições de uma nota no RecyclerView
    inner class NoteViewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notes_layout = itemView.findViewById<CardView>(R.id.card_layout)
        val title = itemView.findViewById<TextView>(R.id.tv_title)
        val note_tv = itemView.findViewById<TextView>(R.id.tv_note)
        val date = itemView.findViewById<TextView>(R.id.tv_date)
    }

    // Interface para lidar com eventos de clique nos itens do RecyclerView
    interface NotesClickListener {
        fun onItemClicked(note: Note)
        fun onLongItemClicked(note: Note, cardView: CardView)
    }

    open class NotesItemClickListener {

    }
}
