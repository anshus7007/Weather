package com.anshu.weather.db.db1_for_city_name.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun upsert(item: CityWeather)

    @Delete
     suspend fun delete(item: CityWeather)

    @Query("SELECT * FROM city_weather")
    fun getAllMoneyItems():LiveData<List<CityWeather>>
}