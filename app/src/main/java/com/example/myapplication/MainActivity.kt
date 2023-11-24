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



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FlashcardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val flashcards = LerncartaDataSource().flashcards

        adapter = FlashcardAdapter(flashcards)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.addButton.setOnClickListener {
            val addDialogView = LayoutInflater.from(this).inflate(R.layout.add_flashcard_dialog, null)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Neue Flashcard hinzufügen")
                .setView(addDialogView)
                .setPositiveButton("Hinzufügen", null)
                .setNegativeButton("Abbrechen", null)
                .show()

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                // Hier greifst du auf die Textfelder des Dialogs zu
                val questionInput = addDialogView.findViewById<EditText>(R.id.questionInput)
                val answerInput = addDialogView.findViewById<EditText>(R.id.answerInput)
                val question = questionInput.text.toString().trim()
                val answer = answerInput.text.toString().trim()

                // Prüfe, ob die Eingaben gültig sind
                if (question.isNotEmpty() && answer.isNotEmpty()) {
                    val newFlashcard = Flashcard(question, answer)
                    (adapter.flashcards as MutableList).add(newFlashcard)
                    adapter.notifyItemInserted(adapter.flashcards.size - 1)
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Frage und Antwort dürfen nicht leer sein.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.editButton.setOnClickListener {
            if (adapter.flashcards.isNotEmpty()) {
                // Wähle hier, welche Flashcard bearbeitet werden soll
                val positionToEdit = 0 // Dies solltest du entsprechend anpassen
                val flashcardToEdit = adapter.flashcards[positionToEdit]

                val editDialogView = LayoutInflater.from(this).inflate(R.layout.edit_flashcard_dialog, null)
                val questionInput = editDialogView.findViewById<EditText>(R.id.questionInput).apply {
                    setText(flashcardToEdit.question)
                }
                val answerInput = editDialogView.findViewById<EditText>(R.id.answerInput).apply {
                    setText(flashcardToEdit.answer)
                }

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
                        (adapter.flashcards as MutableList)[positionToEdit] = updatedFlashcard
                        adapter.notifyItemChanged(positionToEdit)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(this, "Frage und Antwort dürfen nicht leer sein.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.deleteButton.setOnClickListener {
            if (adapter.flashcards.isNotEmpty()) {
                // Wähle hier, welche Flashcard gelöscht werden soll
                val positionToDelete = 0 // Dies solltest du entsprechend anpassen
                AlertDialog.Builder(this)
                    .setTitle("Flashcard löschen")
                    .setMessage("Bist du sicher, dass du diese Flashcard löschen möchtest?")
                    .setPositiveButton("Löschen") { dialog, which ->
                        (adapter.flashcards as MutableList).removeAt(positionToDelete)
                        adapter.notifyItemRemoved(positionToDelete)
                    }
                    .setNegativeButton("Abbrechen", null)
                    .show()
            }
        }

    }
}