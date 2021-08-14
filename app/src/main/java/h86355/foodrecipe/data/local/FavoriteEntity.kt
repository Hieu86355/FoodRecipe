package h86355.foodrecipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodrecipes.models.Result
import h86355.foodrecipe.utils.Constants.Companion.FAVORITE_TABLE_NAME

@Entity(tableName = FAVORITE_TABLE_NAME)
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)