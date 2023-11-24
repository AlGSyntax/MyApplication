package com.example.myapplication

import Itemadapter.FlashcardAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import data.LerncartaDataSource
import model.Flashcard
// Hauptaktivität für die Flashcard-App.
class MainActivity : AppCompatActivity() {
    // Bindung für die View-Elemente dieser Aktivität.
    private lateinit var binding: ActivityMainBinding
    // Adapter für die Verwaltung der Daten in der RecyclerView.
    private lateinit var adapter: FlashcardAdapter
    // Die Datenquelle für die Flashcards.
    private val flashcards = LerncartaDataSource().flashcards.toMutableList()

    // onCreate-Methode wird aufgerufen, wenn die Aktivität erstellt wird.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisiere die View-Bindung.

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Richte die RecyclerView und die Button-Listener ein.
        setupRecyclerView()
        setupButtonListeners()
    }


    // Hilfsmethode zum Einrichten der RecyclerView.
    private fun setupRecyclerView() {
        // Initialisiere den Adapter mit den Flashcards und den Callbacks für Bearbeiten/Löschen.
        adapter = FlashcardAdapter(flashcards,
            onEdit = { flashcard, position ->
                editFlashcard(flashcard, position)
            },
            onDelete = { position ->
                flashcards.removeAt(position)
                adapter.notifyItemRemoved(position)
            }
        )
        // Setze den LayoutManager und den Adapter für die RecyclerView.
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    // Hilfsmethode zum Einrichten der Button-Listener.
    private fun setupButtonListeners() {
        binding.addButton.setOnClickListener {
            addNewFlashcard()
        }
    }


    // Zeigt ein Dialogfenster zum Hinzufügen einer neuen Flashcard an.
    private fun addNewFlashcard() {
        // Dialoglayout wird aufgeblasen und konfiguriert
        val addDialogView = LayoutInflater.from(this).inflate(R.layout.add_flashcard_dialog, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle("Neue Flashcard hinzufügen")
            .setView(addDialogView)
            .setPositiveButton("Hinzufügen", null)
            .setNegativeButton("Abbrechen", null)
            .show()


        // Listener für den "Hinzufügen"-Button im Dialog.
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val questionInput = addDialogView.findViewById<EditText>(R.id.questionInput)
            val answerInput = addDialogView.findViewById<EditText>(R.id.answerInput)
            val question = questionInput.text.toString().trim()
            val answer = answerInput.text.toString().trim()


            // Überprüfung, ob Frage und Antwort nicht leer sind.

            if (question.isNotEmpty() && answer.isNotEmpty()) {
                val newFlashcard = Flashcard(question, answer)
                flashcards.add(newFlashcard)
                adapter.notifyItemInserted(flashcards.size - 1)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Frage und Antwort dürfen nicht leer sein.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    // Öffnet ein Dialogfenster zum Bearbeiten einer vorhandenen Flashcard.
    private fun editFlashcard(flashcard: Flashcard, position: Int) {
        val editDialogView = LayoutInflater.from(this).inflate(R.layout.edit_flashcard_dialog, null)
        // Setzt die aktuellen Werte der Flashcard in die Textfelder.
        val questionInput = editDialogView.findViewById<EditText>(R.id.questionInput)
        val answerInput = editDialogView.findViewById<EditText>(R.id.answerInput)
        questionInput.setText(flashcard.question)
        answerInput.setText(flashcard.answer)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Flashcard bearbeiten")
            .setView(editDialogView)
            .setPositiveButton("Aktualisieren", null)
            .setNegativeButton("Abbrechen", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val updatedQuestion = questionInput.text.toString().trim()
            val updatedAnswer = answerInput.text.toString().trim()

            if (updatedQuestion.isNotEmpty() && updatedAnswer.isNotEmpty()) {
                val updatedFlashcard = Flashcard(updatedQuestion, updatedAnswer)
                flashcards[position] = updatedFlashcard
                adapter.notifyItemChanged(position)
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Frage und Antwort dürfen nicht leer sein.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
