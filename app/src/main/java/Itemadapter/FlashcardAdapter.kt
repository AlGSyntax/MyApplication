package Itemadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemBinding
import model.Flashcard

class FlashcardAdapter(
    val flashcards: MutableList<Flashcard>,
    private val onEdit: (Flashcard, Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    // ViewBinding für die list_item.xml
    inner class FlashcardViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.editIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEdit(flashcards[position], position)
                }
            }

            // Setze den OnClickListener für das Löschen-Icon
            binding.deleteIcon.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDelete(position)
                }
            }
        }


        //Binden
        fun bind(flashcard: Flashcard) {
            binding.textViewQuestion.text = flashcard.question
            binding.textViewAnswer.text = flashcard.answer

            // OnClickListener für die Karte, um die Antwort ein-/auszublenden
            binding.materialCardView.setOnClickListener {
                if (binding.textViewAnswer.visibility == View.GONE) {
                    binding.textViewAnswer.visibility = View.VISIBLE
                } else {
                    binding.textViewAnswer.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return FlashcardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashcardViewHolder, position: Int) {
        val flashcard = flashcards[position]
        holder.bind(flashcard)
    }

    override fun getItemCount(): Int = flashcards.size
}
