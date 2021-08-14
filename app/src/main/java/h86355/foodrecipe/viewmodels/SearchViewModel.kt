package h86355.foodrecipe.viewmodels

import androidx.lifecycle.ViewModel
import h86355.foodrecipe.utils.Constants
import h86355.foodrecipe.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import h86355.foodrecipe.utils.Constants.Companion.QUERY_API_KEY
import h86355.foodrecipe.utils.Constants.Companion.QUERY_DIET
import h86355.foodrecipe.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import h86355.foodrecipe.utils.Constants.Companion.QUERY_NUMBER
import h86355.foodrecipe.utils.Constants.Companion.QUERY_SEARCH
import h86355.foodrecipe.utils.Constants.Companion.QUERY_TYPE

class SearchViewModel: ViewModel() {

    var mealType = ""
    var diet = ""
    var query = ""

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_NUMBER] = "20"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_SEARCH] = query
        if (!mealType.equals("none")) {
            queries[QUERY_TYPE] = mealType
        }
        if (!diet.equals("none")) {
            queries[QUERY_DIET] = diet
        }
        return queries
    }

}