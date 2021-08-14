package com.example.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.models.Result
import h86355.foodrecipe.adapters.RecipeClickListener
import h86355.foodrecipe.databinding.RowRecipeBinding

class RecipeRecyclerAdapter(
    val recipeClickListener: RecipeClickListener
) : ListAdapter<Result, RecipeRecyclerAdapter.RecipeHolder>(RecipeDiffCallback()) {

    class RecipeHolder(
        private val binding: RowRecipeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result, recipeClickListener: RecipeClickListener) {
            binding.result = result
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                recipeClickListener.onRecipeClick(result)
            }
        }

        companion object {
            fun from(parent: ViewGroup): RecipeHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowRecipeBinding.inflate(layoutInflater, parent, false)
                return RecipeHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeHolder {
        return RecipeHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, recipeClickListener)
    }

}

class RecipeDiffCallback : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }

}
