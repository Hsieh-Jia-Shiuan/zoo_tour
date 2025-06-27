package com.example.zoo_tour.model.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImportDate(
    @SerializedName("date") val date: String?,
    @SerializedName("timezone_type") val timezoneType: Int?,
    @SerializedName("timezone") val timezone: String?
) : Parcelable