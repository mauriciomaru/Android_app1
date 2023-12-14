package com.example.android_app.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidade que representa uma nota no banco de dados
@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int?,  // Chave primária única, autoincrementada
    @ColumnInfo(name = "title") val title: String?,  // Coluna para armazenar o título da nota
    @ColumnInfo(name = "note") val note: String?,    // Coluna para armazenar o conteúdo da nota
    @ColumnInfo(name = "date") val date: String?      // Coluna para armazenar a data da nota
) : java.io.Serializable  // Implementação da interface Serializable para permitir a passagem de objetos via Intent

