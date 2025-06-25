package com.example.zoo_tour.model.dataSource

import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ZooApiService {
    // 館區資料 API
    @GET("dataset/5a0e5fbb-72f8-41c6-908e-2fb25eff9b8a")
    suspend fun getAreas(
        @Query("scope") scope: String = "resourceAquire"
    ): ApiResponse<Area>

    // 動物資料 API
    @GET("dataset/a3e2b221-75e0-45c1-8f97-75acbd43d613")
    suspend fun getAnimals(
        @Query("scope") scope: String = "resourceAquire"
    ): ApiResponse<Animal>

    // 植物資料 API
    @GET("dataset/e20706d8-bf89-4e6a-9768-db2a10bb2ba4")
    suspend fun getPlants(
        @Query("scope") scope: String = "resourceAquire"
    ): ApiResponse<Plant>
}