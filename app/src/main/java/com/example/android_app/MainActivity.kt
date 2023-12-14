package com.example.android_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android_app.Adapter.NotesAdapter
import com.example.android_app.Database.NoteDatabase
import com.example.android_app.Models.Note
import com.example.android_app.Models.NoteViewModel
import com.example.android_app.databinding.ActivityMainBinding

class MainActivity() : AppCompatActivity(), NotesAdapter.NotesItemClickListener(), PopupMenu.OnMenuItemClickListener,
    Parcelable {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase

    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note

    // Registrar um contrato de atividade para atualizar uma nota existente
    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.getSerializableExtra("note")?.let { updatedNote ->
                // Atualizar a nota no ViewModel ao receber o resultado
                viewModel.updateNote(updatedNote as Note)
            }
        }
    }

    constructor(parcel: Parcel) : this() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar a tela principal
        initScreen()

        // Inicializar o ViewModel e observar as alterações na lista de notas
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(
            NoteViewModel::class.java
        )

        viewModel.allNotes.observe(this) { list ->
            list?.let {
                // Atualizar a lista de notas exibida no RecyclerView
                adapter.updateList(list)
            }
        }

        // Inicializar o banco de dados da nota
        database = NoteDatabase.getDatabase(this)
    }

    private fun initScreen() {
        // Configurar o RecyclerView
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        // Registrar um contrato de atividade para obter o resultado ao adicionar uma nova nota
        val getContent =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.getSerializableExtra("note")?.let { newNote ->
                        // Inserir a nova nota no ViewModel ao receber o resultado
                        viewModel.insertNote(newNote as Note)
                    }
                }
            }

        // Configurar o método de clique longo no botão de adicionar nota para adicionar uma nova nota
        binding.fbAddNote.setOnLongClickListener {
            val intent = Intent(this, AddNote::class.java)
            getContent.launch(intent)
            true
        }

        // Configurar o ouvinte de texto de pesquisa na barra de pesquisa
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(newText: String?): Boolean {
                newText?.let {
                    // Filtrar a lista de notas com base no texto de pesquisa
                    adapter.filterList(it)
                }
                return true
            }
        })
    }

    // Método de clique curto em uma nota
    fun onItemClick(note: Note) {
        val intent = Intent(this@MainActivity, AddNote::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    // Método de clique longo em uma nota
    fun onItemLongClick(note: Note, cardView: CardView) {
        selectedNote = note
        // Exibir o menu pop-up com opções (editar, excluir)
        showPopupMenu(cardView)
    }

    // Exibir o menu pop-up com opções (editar, excluir)
    private fun showPopupMenu(cardView: CardView) {
        val popup = PopupMenu(this, cardView)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    // Método de clique nos itens do menu pop-up (editar, excluir)
    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.delete_note) {
            // Excluir a nota selecionada ao clicar em "Excluir"
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }
}