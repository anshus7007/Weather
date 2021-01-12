package com.anshu.weather.city_view_model

import androidx.lifecycle.ViewModel
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.repositories.CityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CityViewModel(private val repository: CityRepository
): ViewModel() {

    fun insert(item: CityWeather)= CoroutineScope(Dispatchers.Main).launch {
        repository.insert(item)
    }
    fun delete(item: CityWeather)= CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }

    fun getAllSearchedCity() = repository.getAllSearchedCity();
}