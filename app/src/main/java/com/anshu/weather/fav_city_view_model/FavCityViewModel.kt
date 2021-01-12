package com.anshu.weather.fav_city_view_model

import androidx.lifecycle.ViewModel
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity
import com.anshu.weather.repositories.CityRepository
import com.anshu.weather.repositories.FavCityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavCityViewModel(private val repository: FavCityRepository
): ViewModel() {

    fun insert(item: FavCity)= CoroutineScope(Dispatchers.Main).launch {
        repository.insert(item)
    }
    fun delete(item: FavCity)= CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }
    fun getCityCount()=CoroutineScope(Dispatchers.Main).launch {
        repository.getCityCount()
    }
    fun getCityById(id:Int)= repository.getCityById(id)

    fun getAllFavCity() = repository.getAllFavCity();
}