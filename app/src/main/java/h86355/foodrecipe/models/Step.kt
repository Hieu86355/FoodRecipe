package com.example.foodrecipes.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    @SerializedName("number")
    val number: Int?,
    @SerializedName("step")
    val step: String?
): Parcelable