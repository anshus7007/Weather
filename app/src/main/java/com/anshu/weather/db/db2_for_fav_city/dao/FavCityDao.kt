package com.anshu.weather.db.db2_for_fav_city.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity

@Dao
interface FavCityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: FavCity)

    @Delete
    suspend fun delete(item: FavCity)
    @Query("SELECT COUNT(*) FROM fav_city")
    suspend fun getCount():Int
    @Query("SELECT city FROM fav_city WHERE id = :id")
     fun getCityById(id:Int):String
    @Query("SELECT * FROM fav_city")
    fun getAllFavCity(): LiveData<MutableList<FavCity>>
}