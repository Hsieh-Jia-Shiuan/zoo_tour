package com.example.zoo_tour.util

import com.google.gson.annotations.SerializedName

data class ApiResult<T>(
    @SerializedName("limit") val limit: Int,
    @SerializedName("offset") val offset: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("sort") val sort: String,
    @SerializedName("results") val results: List<T>
)
