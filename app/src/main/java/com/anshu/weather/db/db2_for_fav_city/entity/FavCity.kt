package com.anshu.weather.db.db2_for_fav_city.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_city")
data class  FavCity(
        @PrimaryKey
        var id: Int? = null,
        @ColumnInfo(name = "city")
        var name: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "temp")
    var temp: String

)