package com.example.foodrecipes.models


import android.os.Parcelable
import com.example.foodrecipes.models.Step
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class AnalyzedInstruction(
    @SerializedName("name")
    val name: String?,
    @SerializedName("steps")
    val steps: @RawValue List<Step>?
) : Parcelable