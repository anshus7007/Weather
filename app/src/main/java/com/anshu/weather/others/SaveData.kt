package com.anshu.weather.others

import android.content.Context
import android.content.SharedPreferences

class SaveData(context: Context) {
    private val sharedPref:SharedPreferences=context.getSharedPreferences("file_name",Context.MODE_PRIVATE)

    fun setDarkModeState(state:Boolean?)
    {
        val edtor=sharedPref.edit()
        edtor.putBoolean("Dark",state!!)
        edtor.commit()
    }
    fun loaddarkModeState():Boolean?
    {
        val state=sharedPref.getBoolean("Dark",false)
        return state
    }
}