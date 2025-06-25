package com.example.zoo_tour.model.entities

import com.google.gson.annotations.SerializedName

data class ImportDate(
    @SerializedName("date") val date: String,
    @SerializedName("timezone_type") val timezoneType: Int,
    @SerializedName("timezone") val timezone: String
)