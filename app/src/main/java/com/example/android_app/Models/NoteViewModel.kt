package com.example.android_app.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.android_app.Database.NoteDatabase
import com.example.android_app.Database.NotesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// ViewModel responsável por interagir com a camada de repositório e fornecer dados à interface do usuário
class NoteViewModel(application: Application) : AndroidViewModel(application) {

    // Repositório que lida com as operações no banco de dados
    private val repository: NotesRepository

    // Escopo de coroutine para executar operações em segundo plano
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    // LiveData contendo a lista de todas as notas no banco de dados
    val allNotes: LiveData<List<Note>>

    init {
        // Inicializar o DAO e o repositório ao criar a instância do ViewModel
        val dao = NoteDatabase.getDatabase(application).getNoteDao()
        repository = NotesRepository(dao)
        allNotes = repository.allNotes
    }

    // Método para excluir uma nota do banco de dados
    fun deleteNote(note: Note) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.delete(note)
        }
    }

    // Método para inserir uma nova nota no banco de dados
    fun insertNote(note: Note) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.insert(note)
        }
    }

    // Método para atualizar uma nota existente no banco de dados
    fun updateNote(note: Note) {
        coroutineScope.launch(Dispatchers.IO) {
            repository.update(note)
        }
    }

    // Método chamado quando o ViewModel é destruído, cancelando todas as corrotinas em execução
    override fun onCleared() {
        super.onCleared()
        coroutineScope.coroutineContext.cancel()
    }
}
