package com.example.zoo_tour.model.dataSource

import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.util.NetworkResult
import retrofit2.HttpException
import java.io.IOException

class RemoteDataSource(
    private val apiService: ZooApiService
) {
    // 獲取館區簡介
    suspend fun fetchAreas(): NetworkResult<List<Area>> {
        return try {
            val response = apiService.getAreas().result.results
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(message = "API error (${e.code()}): ${e.message()}")
        } catch (e: IOException) {
            NetworkResult.Error(message = "Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            NetworkResult.Error(message = "An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    // 獲取動物資料
    suspend fun fetchAnimals(): NetworkResult<List<Animal>> {
        return try {
            val response = apiService.getAnimals().result.results
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(message = "API error (${e.code()}): ${e.message()}")
        } catch (e: IOException) {
            NetworkResult.Error(message = "Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            NetworkResult.Error(message = "An unexpected error occurred: ${e.localizedMessage}")
        }
    }

    // 獲取植物資料
    suspend fun fetchPlants(): NetworkResult<List<Plant>> {
        return try {
            val response = apiService.getPlants().result.results
            NetworkResult.Success(response)
        } catch (e: HttpException) {
            NetworkResult.Error(message = "API error (${e.code()}): ${e.message()}")
        } catch (e: IOException) {
            NetworkResult.Error(message = "Network error: ${e.localizedMessage}")
        } catch (e: Exception) {
            NetworkResult.Error(message = "An unexpected error occurred: ${e.localizedMessage}")
        }
    }
}