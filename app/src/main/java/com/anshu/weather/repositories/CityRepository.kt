package com.anshu.weather.repositories

import com.anshu.weather.db.db1_for_city_name.CityDatabase
import com.anshu.weather.db.db1_for_city_name.dao.CityDao
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather

class CityRepository(private val db: CityDatabase) {

        suspend fun insert(item: CityWeather)=db.getCityDao().upsert(item)

        suspend fun delete(item: CityWeather)= db.getCityDao().delete(item)

        fun getAllSearchedCity() = db.getCityDao().getAllMoneyItems()

}