package h86355.foodrecipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipes.models.FoodRecipe
import h86355.foodrecipe.utils.Constants.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RecipeEntity (var foodRecipe: FoodRecipe) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var mealType: String = ""
}