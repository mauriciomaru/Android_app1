package com.example.android_app.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_app.Models.Note

// Data Access Object (DAO) responsável por definir as operações de acesso ao banco de dados para a entidade Note
@Dao
interface NoteDao {

    // Inserir uma nota no banco de dados, substituindo em caso de conflito
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    // Excluir uma nota do banco de dados
    @Delete
    suspend fun delete(note: Note)

    // Obter todas as notas do banco de dados ordenadas pelo ID em ordem ascendente
    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    fun getAllNotes(): LiveData<List<Note>>

    // Atualizar o título e o conteúdo de uma nota no banco de dados com base no ID
    @Query("UPDATE notes_table SET title = :title, note = :note WHERE id = :id")
    suspend fun update(id: Int?, title: String?, note: String?)
}

