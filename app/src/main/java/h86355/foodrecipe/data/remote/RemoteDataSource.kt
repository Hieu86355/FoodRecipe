package h86355.foodrecipe.data.remote

import com.example.foodrecipes.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipeApi: RecipeApi
) {
    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return recipeApi.getRecipes(queries)
    }
}