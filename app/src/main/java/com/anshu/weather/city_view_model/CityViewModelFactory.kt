package com.anshu.weather.city_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshu.weather.repositories.CityRepository

@Suppress("UNCHECKED_CAST")
class CityViewModelFactory(
    private val repository: CityRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CityViewModel(repository) as T
    }
}