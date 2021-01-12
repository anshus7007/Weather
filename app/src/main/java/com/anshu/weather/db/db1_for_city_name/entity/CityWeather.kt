package com.anshu.weather.db.db1_for_city_name.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "city_weather")
data class CityWeather(
    @ColumnInfo(name = "city_name")
    var name: String,
    @ColumnInfo(name = "wrongRight")
    var wrongRight: Boolean
//    @ColumnInfo(name = "temp")
//    var temp: String

) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}