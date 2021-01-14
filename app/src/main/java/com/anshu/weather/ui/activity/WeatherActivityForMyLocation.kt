package com.anshu.weather.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.anshu.weather.R
import com.anshu.weather.city_view_model.CityViewModel
import com.anshu.weather.city_view_model.CityViewModelFactory
import com.anshu.weather.db.db1_for_city_name.CityDatabase
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.db.db2_for_fav_city.FavCityDB
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity
import com.anshu.weather.fav_city_view_model.FavCityViewModel
import com.anshu.weather.fav_city_view_model.FavCityViewModelFactory
import com.anshu.weather.others.Constants
import com.anshu.weather.repositories.CityRepository
import com.anshu.weather.repositories.FavCityRepository
import com.anshu.weather.util.ConnectionManager
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WeatherActivityForMyLocation : AppCompatActivity() {
    lateinit var progressBar: LottieAnimationView
    lateinit var move_to_search: MaterialCardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_of_my_location)
        val intent = intent
        val city = intent.getStringExtra("city_name")
        progressBar=findViewById(R.id.progressBar_mine)
        move_to_search=findViewById(R.id.materialCardView_mine)
//

        if (city != null) {
            progressBar.visibility= View.VISIBLE
            findViewById<ConstraintLayout>(R.id.constraintLayout_mine).visibility= View.GONE
            findViewById<RelativeLayout>(R.id.relativeLayout_mine).visibility= View.GONE

            WeatherTask(city).execute()
        }
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        findViewById<TextView>(R.id.Cancel_mine).setOnClickListener {
            val intent=(Intent(this,MainActivity::class.java))
            intent.putExtra("settings","true")
            startActivity(intent)
            finish()
        }
        move_to_search.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }


    }

    inner class WeatherTask(val city: String) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {

            super.onPreExecute()


        }


        override fun doInBackground(vararg p0: String?): String? {

            var response: String?

            var APPID = "e55a6c82e17579c3f5a238e30a32977f"
            if (ConnectionManager().checkConnectivity(applicationContext as Context)) {

                try {

                    response =
                        URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$APPID").readText(
                            Charsets.UTF_8
                        )
                } catch (e: Exception) {
                    response = null
                }
                return response

            }
            else {
                val alterDialog = androidx.appcompat.app.AlertDialog.Builder(applicationContext as Context)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Internet Connection can't be establish!")
                alterDialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                    startActivity(settingsIntent)
                }

                alterDialog.setNegativeButton("Exit") { text, listener ->
                    finishAffinity()//closes all the instances of the app and the app closes completely
                }
                alterDialog.setCancelable(false)

                alterDialog.create()
                alterDialog.show()
            }
            return null
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)
            findViewById<LottieAnimationView>(R.id.progressBar_mine).visibility= View.GONE

            try {
                if(result==null)
                {


                    findViewById<ConstraintLayout>(R.id.constraintLayout_mine).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.relativeLayout_mine).visibility = View.GONE


                }
                else {


                    findViewById<ConstraintLayout>(R.id.constraintLayout_mine).visibility= View.GONE
                    findViewById<RelativeLayout>(R.id.relativeLayout_mine).visibility= View.VISIBLE

                    val jsonObj = JSONObject(result)


                    val main = jsonObj.getJSONObject("main")
                    val id=jsonObj.getInt("id")
                    val sys = jsonObj.getJSONObject("sys")
                    val wind = jsonObj.getJSONObject("wind")
                    val clouds=jsonObj.getJSONObject("clouds")
                    val cloudness=clouds.getString("all")
                    val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                    val updatedAt = jsonObj.getLong("dt")


                    val updatedAtText =
                        "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                            Date(updatedAt * 1000)
                        )
                    val updatedAtReq= SimpleDateFormat(" hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                    val temp = main.getString("temp")
                    val tempMin = main.getString("temp_min")
                    val tempMax = main.getString("temp_max")
//                        val cloudness=jsonObj.getJSONArray("cloudness")
                    val pressure = main.getString("pressure")
                    val humidity = main.getString("humidity")
                    val sunrise: Long = sys.getLong("sunrise")
                    val windSpeed = wind.getString("speed")
                    val sunset: Long = sys.getLong("sunset")
                    val description = weather.getString("description")
                    val address = jsonObj.getString("name")

                    findViewById<TextView>(R.id.city_name_mine).text = address
                    findViewById<TextView>(R.id.description_mine).text = description
                    findViewById<TextView>(R.id.temp_mine).text = temp
                    findViewById<TextView>(R.id.min_temp_mine).text = tempMin
                    findViewById<TextView>(R.id.max_temp_mine).text = tempMax
                    findViewById<TextView>(R.id.txtSunrise_mine).text =
                        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                    findViewById<TextView>(R.id.txtSunset_mine).text =
                        SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                    findViewById<TextView>(R.id.txtPressure_mine).text = pressure.toString()+" hPa"
                    findViewById<TextView>(R.id.txtHumidity_mine).text = humidity.toString()+"%"
                    findViewById<TextView>(R.id.txtWind_mine).text = windSpeed.toString()+" m/sc"
                    findViewById<TextView>(R.id.txtCloudness_mine).text = cloudness.toString()+"%"
                    val hrs:Int=SimpleDateFormat("hh", Locale.ENGLISH).format(Date(sunset * 1000)).toInt()
                    val ampm:String=SimpleDateFormat("a", Locale.ENGLISH).format(Date(sunset * 1000))
//                        println("&7777777777777777777777777777777${hrs}${ampm}&%%%%%%%%%%%%%%%%%%%%%%%%")
                    if(description.equals("snow") && hrs>=6&&ampm.equals("PM"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_snowy_night)
                    else if(description.equals("snow") &&ampm.equals("AM"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_snowy)
                    else if(description.contains("rain"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_rain)
                    else if(description.contains("cloud")&&hrs>=6&&hrs<=12&&ampm.equals("PM"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_cloudy_night)
                    else if((description.contains("clouds") &&ampm.equals("AM"))||description==("clear sky"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_cloudy)
                    else if(description.contains("overcast"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_overcast)
                    else if(description.contains("fog")||description.contains("haze")||description.contains("smoke"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_fog__1_)
                    else if(description.contains("mist"))
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_mist)
                    else
                        findViewById<RelativeLayout>(R.id.relativeLayout_mine).background=
                            ContextCompat.getDrawable(applicationContext,R.drawable.ic_scattered_clouds)


                }

            } catch (e: Exception) {

            }
        }



    }

    override fun onResume() {

        if (ConnectionManager().checkConnectivity(this as Context)) {
            val intent = intent
            val city = intent.getStringExtra("city_name")

            if (city != null) {
                if (city != null) {
                    WeatherTask(city).execute()
                }
            }//if internet is available fetch data
        } else {

            val alterDialog = androidx.appcompat.app.AlertDialog.Builder(this as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                finishAffinity()//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()

        }


        super.onResume()
    }
}

