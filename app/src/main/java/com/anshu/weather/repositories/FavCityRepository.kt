package com.anshu.weather.repositories

import com.anshu.weather.db.db1_for_city_name.CityDatabase
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.db.db2_for_fav_city.FavCityDB
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity

class FavCityRepository (private val db: FavCityDB) {

    suspend fun insert(item: FavCity)=db.getFavCity().insert(item)

    suspend fun delete(item: FavCity)= db.getFavCity().delete(item)
    suspend fun getCityCount()=db.getFavCity().getCount()
      fun getCityById(id:Int)=db.getFavCity().getCityById(id)

    fun getAllFavCity() = db.getFavCity().getAllFavCity()

}