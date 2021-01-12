package com.anshu.weather.fav_city_view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anshu.weather.city_view_model.CityViewModel
import com.anshu.weather.repositories.CityRepository
import com.anshu.weather.repositories.FavCityRepository

@Suppress("UNCHECKED_CAST")
class FavCityViewModelFactory(
    private val repository: FavCityRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavCityViewModel(repository) as T
    }
}