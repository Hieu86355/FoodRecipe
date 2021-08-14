package h86355.foodrecipe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.models.ExtendedIngredient
import h86355.foodrecipe.databinding.RowIngredientsBinding

class IngredientRecyclerAdapter :
    ListAdapter<ExtendedIngredient, IngredientRecyclerAdapter.IngredientHolder>(
        IngredientDiffCallback()
    ) {

    class IngredientHolder(
        private val binding: RowIngredientsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: ExtendedIngredient) {
            binding.ingredient = ingredient
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): IngredientHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowIngredientsBinding.inflate(layoutInflater, parent, false)
                return IngredientHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientHolder {
        return IngredientHolder.from(parent)
    }

    override fun onBindViewHolder(holder: IngredientHolder, position: Int) {
        val currentIngredient = getItem(position)
        holder.bind(currentIngredient)
    }

}

class IngredientDiffCallback : DiffUtil.ItemCallback<ExtendedIngredient>() {
    override fun areItemsTheSame(
        oldItem: ExtendedIngredient,
        newItem: ExtendedIngredient
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ExtendedIngredient,
        newItem: ExtendedIngredient
    ): Boolean {
        return oldItem == newItem
    }

}