package com.example.zoo_tour.util

import com.google.gson.annotations.SerializedName

data class ApiResponse<T>(
    @SerializedName("result") val result: ApiResult<T>
)