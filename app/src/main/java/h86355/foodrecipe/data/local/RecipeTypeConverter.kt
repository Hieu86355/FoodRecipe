package h86355.foodrecipe.data.local

import androidx.room.TypeConverter
import com.example.foodrecipes.models.FoodRecipe
import com.example.foodrecipes.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipeTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun convertRecipesToStr(foodRecipe: FoodRecipe) : String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun convertStrToRecipes(strJson: String) : FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(strJson, listType)
    }

    @TypeConverter
    fun convertResultToStr(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun convertStrToResult(strJson: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(strJson, listType)
    }
}