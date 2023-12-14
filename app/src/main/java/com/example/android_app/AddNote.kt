package com.example.android_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.android_app.Models.Note
import com.example.android_app.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddNote : AppCompatActivity() {

    // Binding para a atividade
    private lateinit var binding: ActivityMainBinding

    // Nota atual sendo editada
    private lateinit var note: Note

    // Nota antiga (se estiver atualizando uma nota existente)
    private lateinit var oldNote: Note

    // Indicador para rastrear se é uma atualização ou uma nova nota
    private var isUpdate = false

    // Vistas
    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    private lateinit var imgBackArrow: ImageView
    private lateinit var imgCheck: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar o binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            // Verificar se há uma nota existente passada pela intent
            oldNote = intent.getSerializableExtra("current_note") as Note
            with(binding) {
                etTitle = findViewById(R.id.etTitle)
                etNote = findViewById(R.id.etNote)
                imgBackArrow = findViewById(R.id.imgBackArrow)
                imgCheck = findViewById(R.id.imgCheck)

                // Preencher a UI com os dados da nota existente
                etTitle.setText(oldNote.title)
                etNote.setText(oldNote.note)
            }
            isUpdate = true
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Configurar o manipulador de clique para o botão de marca de seleção
        imgCheck.setOnClickListener {

            // Obter título e descrição da nota a partir dos campos de entrada
            val title = etTitle.text.toString()
            val noteDesc = etNote.text.toString()

            // Verificar se o título ou a descrição da nota não estão vazios
            if (title.isNotEmpty() || noteDesc.isNotEmpty()) {

                // Formatar a data
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

                // Criar uma nova nota ou atualizar a existente
                note = if (isUpdate) {
                    Note(oldNote.id, title, noteDesc, formatter.format(Date()))
                } else {
                    Note(null, title, noteDesc, formatter.format(Date()))
                }

                // Definir o resultado e encerrar a atividade
                val intent = Intent().apply {
                    putExtra("note", note)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()

            } else {
                // Exibir uma mensagem de toast se tanto o título quanto a descrição da nota estiverem vazios
                Toast.makeText(this@AddNote, "Por favor, insira os dados", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar o manipulador de clique para o botão de seta para trás
        imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }

    // Função de extensão para simplificar a configuração de manipuladores de clique para ImageViews
    private fun ImageView.setCliqueHandler(function: () -> Unit) {
        setOnClickListener { function.invoke() }
    }
}