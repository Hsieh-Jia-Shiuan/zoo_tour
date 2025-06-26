package com.example.zoo_tour.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zoo_tour.model.repositories.ZooRepository

// 通用的 ViewModelFactory
class ZooViewModelFactory(
    private val repository: ZooRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AreaListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AreaListViewModel(repository) as T
        }

        if (modelClass.isAssignableFrom(ExhibitDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ExhibitDataViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}