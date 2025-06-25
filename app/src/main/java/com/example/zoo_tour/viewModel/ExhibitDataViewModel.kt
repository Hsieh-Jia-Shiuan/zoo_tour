package com.example.zoo_tour.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoo_tour.model.entities.Animal
import com.example.zoo_tour.model.entities.Plant
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExhibitDataViewModel(
    private val repository: ZooRepository
) : ViewModel() {
    private val _animals = MutableStateFlow<NetworkResult<List<Animal>>>(NetworkResult.Loading())
    val animals: StateFlow<NetworkResult<List<Animal>>> = _animals

    private val _plants = MutableStateFlow<NetworkResult<List<Plant>>>(NetworkResult.Loading())
    val plants: StateFlow<NetworkResult<List<Plant>>> = _plants

    // 獲取動物資料
    fun fetchAnimals() {
        viewModelScope.launch {
            repository.getAnimals().collect { result ->
                _animals.value = result
            }
        }
    }

    // 獲取植物資料
    fun fetchPlants() {
        viewModelScope.launch {
            repository.getPlants().collect { result ->
                _plants.value = result
            }
        }
    }
}