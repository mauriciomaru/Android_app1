package com.example.android_app.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android_app.Models.Note
import com.example.android_app.Utilities.DATABASE_NAME

// Classe abstrata que representa o banco de dados Room para notas
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    // Método abstrato para obter o DAO associado ao banco de dados
    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        // Método estático para obter uma instância do banco de dados (usando o padrão Singleton)
        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {

                // Construir uma nova instância do banco de dados se não existir uma
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
