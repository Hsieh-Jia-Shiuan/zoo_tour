package com.example.zoo_tour.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zoo_tour.model.entities.Area
import com.example.zoo_tour.model.repositories.ZooRepository
import com.example.zoo_tour.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AreaListViewModel(
    private val repository: ZooRepository
) : ViewModel() {
    private val _areas = MutableStateFlow<NetworkResult<List<Area>>>(NetworkResult.Loading())
    val areas: StateFlow<NetworkResult<List<Area>>> = _areas

    init {
        fetchAreas()
    }

    // 獲取館區簡介
    fun fetchAreas() {
        viewModelScope.launch {
            repository.getAreas().collect { result ->
                _areas.value = result
            }
        }
    }
}