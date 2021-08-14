package h86355.foodrecipe.viewmodels

import androidx.lifecycle.*
import h86355.foodrecipe.utils.Constants.Companion.API_KEY
import h86355.foodrecipe.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import h86355.foodrecipe.utils.Constants.Companion.QUERY_API_KEY
import h86355.foodrecipe.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import h86355.foodrecipe.utils.Constants.Companion.QUERY_NUMBER
import h86355.foodrecipe.utils.Constants.Companion.QUERY_TYPE

class HomeViewModel : ViewModel() {

    private var _loadingVisibility = MutableLiveData<Boolean>()
    val loadingVisibility: LiveData<Boolean> get() = _loadingVisibility

    var mealType= ""

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = "50"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        if (!mealType.equals("all")) {
            queries[QUERY_TYPE] = mealType
        }
       // queries[QUERY_DIET] = diet
        return queries
    }

    fun showLoadingEffect() {
        _loadingVisibility.value = true
    }

    fun hideLoadingEffect() {
        _loadingVisibility.value = false
    }


}