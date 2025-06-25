package com.example.zoo_tour.model.repositories

import com.example.zoo_tour.model.dataSource.RemoteDataSource
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ZooRepository(private val remoteDataSource: RemoteDataSource) {

    fun getAreas(): Flow<NetworkResult<List<Area>>> = flow {
        emit(NetworkResult.Loading())
        val result = remoteDataSource.fetchAreas()
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun getAnimals(): Flow<NetworkResult<List<Animal>>> = flow {
        emit(NetworkResult.Loading())
        val result = remoteDataSource.fetchAnimals()
        emit(result)
    }.flowOn(Dispatchers.IO)

    fun getPlants(): Flow<NetworkResult<List<Plant>>> = flow {
        emit(NetworkResult.Loading())
        val result = remoteDataSource.fetchPlants()
        emit(result)
    }.flowOn(Dispatchers.IO)
}