package h86355.foodrecipe.adapters

import com.example.foodrecipes.models.Result

interface RecipeClickListener {
    fun onRecipeClick(result: Result)
}