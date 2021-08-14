package com.example.foodrecipes.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Result (
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?,
    @SerializedName("analyzedInstructions")
    val analyzedInstructions: @RawValue List<AnalyzedInstruction>?,
    @SerializedName("creditsText")
    val creditsText: String?,
    @SerializedName("extendedIngredients")
    val extendedIngredients: @RawValue List<ExtendedIngredient>?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("license")
    val license: String?,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int?,
    @SerializedName("servings")
    val servings: Int?,
    @SerializedName("sourceUrl")
    val sourceUrl: String?,
    @SerializedName("summary")
    val summary: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vegetarian")
    val vegetarian: Boolean?,
    @SerializedName("dairyFree")
    val dairyFree: Boolean?,
    @SerializedName("glutenFree")
    val glutenFree: Boolean?
): Parcelable
