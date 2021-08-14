package h86355.foodrecipe.data.local

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipeDao: RecipeDao
) {

    suspend fun insertRecipe(entity: RecipeEntity) {
        recipeDao.insertRecipe(entity)
    }

    fun readRecipe(type: String): Flow<List<RecipeEntity>> {
        return recipeDao.readRecipe(type)
    }

    fun readFavoriteRecipe(): Flow<List<FavoriteEntity>> {
        return recipeDao.readFavoriteRecipe()
    }

    suspend fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipeDao.insertFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        recipeDao.deleteFavoriteRecipe(favoriteEntity)
    }

    suspend fun deleteAllFavorite() {
        recipeDao.deleteAllFavorite()
    }


}