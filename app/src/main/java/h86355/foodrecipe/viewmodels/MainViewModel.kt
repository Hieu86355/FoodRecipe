package h86355.foodrecipe.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.example.foodrecipes.models.FoodRecipe
import com.example.foodrecipes.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import h86355.foodrecipe.data.Repository
import h86355.foodrecipe.data.local.FavoriteEntity
import h86355.foodrecipe.data.local.RecipeEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository,
): AndroidViewModel(application) {

    val navigateToSearch = MutableLiveData<Boolean>(false)

    /**
     *  Room Database
     */


    fun readRecipe(type: String): LiveData<List<RecipeEntity>> {
        val recipes = repository.local.readRecipe(type)
        return recipes.asLiveData()
    }

    fun readFavoriteRecipe(): LiveData<List<FavoriteEntity>> {
        val recipes = repository.local.readFavoriteRecipe()
        return recipes.asLiveData()
    }

    private fun insertRecipe(entity: RecipeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipe(entity)
        }
    }

    fun insertFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipe(favoriteEntity)
        }
    }

    fun deleteFavoriteRecipe(favoriteEntity: FavoriteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoriteEntity)
        }
    }

    fun deleteAllFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavorite()
        }
    }

    fun cacheRecipe(foodRecipe: FoodRecipe, mealType: String) {
        val entity = RecipeEntity(foodRecipe).apply {
            this.mealType = mealType
        }
        insertRecipe(entity)
    }


    /**
     * Retrofit
     */

    private var _recipesResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    val recipeResponse: LiveData<NetworkResult<FoodRecipe>> get() = _recipesResponse

    private var _searchResponse = MutableLiveData<NetworkResult<FoodRecipe>>()
    val searchResponse: LiveData<NetworkResult<FoodRecipe>> get() = _searchResponse

    fun getRecipes(queries: Map<String, String>) {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    val response = repository.remote.getRecipes(queries)
                    _recipesResponse.value = handleRecipesResponse(response)

                } catch (e: Exception) {
                    _recipesResponse.value = NetworkResult.Error(e.message.toString())
                }
            } else {
                _recipesResponse.value = NetworkResult.Error("No internet connection")
            }
        }
    }

    fun getSearch(searchQuery: Map<String, String>) {
        viewModelScope.launch {
            if (hasInternetConnection()) {
                try {
                    val response = repository.remote.getRecipes(searchQuery)
                    _searchResponse.value = handleRecipesResponse(response)

                } catch (e: Exception) {
                    _searchResponse.value = NetworkResult.Error(e.message.toString())
                }
            } else {
                _searchResponse.value = NetworkResult.Error("No internet connection")
            }
        }
    }

    // Handle response
    private fun handleRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("ApiKey limited")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }

            response.isSuccessful -> {
                val data = response.body()
                return NetworkResult.Success(data!!)
            }

            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    // Check internet connection
    fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}