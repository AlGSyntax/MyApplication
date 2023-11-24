package Itemadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ListItemBinding
import model.Flashcard

class FlashcardAdapter(
    open val flashcards: List<Flashcard>
) : RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder>() {

    // ViewBinding für die list_item.xml
    inner class FlashcardViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(flashcard: Flashcard) {
            binding.textViewQuestion.text = flashcard.question
            binding.textViewAnswer.text = flashcard.answer

            // OnClickListener für die Karte, um die Antwort ein-/auszublenden
            itemView.setOnClickListener {
                if (binding.textViewAnswer.visibility == View.GONE) {
                    binding.textViewAnswer.visibility = View.VISIBLE
                } else {
                    binding.textViewAnswer.visibility = View.GONE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardViewHolder {
        // Erstellen des ViewBindings für die list_item.xml
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
