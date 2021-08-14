package com.example.foodrecipes.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtendedIngredient(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("amount")
    val amount: Double?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("nameClean")
    val name: String?,
    @SerializedName("original")
    val original: String?,
    @SerializedName("unit")
    val unit: String?
) : Parcelable