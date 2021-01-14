//package com.anshu.weather.others
//
//import android.app.Application
//import android.content.res.Configuration
//
//class InitApplication:Application() {
//    private var isNightModeEnabled = false
//
//    fun getInstance(): InitApplication? {
//        if (InitApplication.singleton == null) {
//            InitApplication.singleton = InitApplication()
//        }
//        return InitApplication.singleton
//    }
//    override fun onCreate() {
//        super.onCreate()
//        val mPrefs = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE)
//        if (mPrefs.getBoolean(Constants.IS_THEME_SET, false)) {
//            isNightModeEnabled = mPrefs.getBoolean(Constants.IS_DARK, true)
//        } else {
//            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
//                Configuration.UI_MODE_NIGHT_NO -> isNightModeEnabled = false
//                Configuration.UI_MODE_NIGHT_YES -> isNightModeEnabled = true
//            }
//        }
//
//    }
//    fun isNightModeEnabled(): Boolean {
//        return isNightModeEnabled
//    }
//    fun setIsNightModeEnabled(isNightModeEnabled: Boolean) {
//        this.isNightModeEnabled = isNightModeEnabled
//        val mPrefs = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE)
//        val editor = mPrefs.edit()
//        editor.putBoolean(Constants.IS_DARK, isNightModeEnabled)
//        editor.putBoolean(Constants.IS_THEME_SET, true)
//        editor.apply()
//    }
//}