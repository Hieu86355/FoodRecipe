package h86355.foodrecipe.utils

class Constants {
    companion object {

        const val API_KEY = "c6e9c253839540ce9ea77c2509089784"
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        const val MEAL_TYPE_ARGUMENT_KEY = "mealType_argument_key"
        const val RECIPE_BUNDLE_KEY = "recipeBundle"

        // Room database
        const val DATABASE_NAME = "recipes_database"
        const val TABLE_NAME = "recipes_table"
        const val FAVORITE_TABLE_NAME = "favorite_recipes_table"

        // Api queries key
        const val QUERY_SEARCH = "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_OFFSET = "0"

        // Preferences datastore
        const val PREFERENCES_NAME = "recipes_filter"
        const val PREFERENCES_BACK_ONLINE = "backOnline"


    }
}