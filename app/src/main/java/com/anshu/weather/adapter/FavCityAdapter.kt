package com.anshu.weather.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.AsyncTask
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.lottie.LottieAnimationView
import com.anshu.weather.R
import com.anshu.weather.db.db1_for_city_name.CityDatabase
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity
import com.anshu.weather.fav_city_view_model.FavCityViewModel
import com.anshu.weather.others.Constants
import com.anshu.weather.ui.activity.HomeActivity
import com.anshu.weather.ui.activity.MainActivity
import com.anshu.weather.ui.activity.WeatherActivity
import com.anshu.weather.util.ConnectionManager
import com.google.android.material.card.MaterialCardView
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class FavCityAdapter(val context: Context,var size:Int,
                     var item: MutableList<FavCity>,var viewModel: FavCityViewModel) : RecyclerView.Adapter<FavCityAdapter.FavCityViewHolder>(){

    fun removeAt(position: Int) {
        Constants.favCityList.remove(item[position].name)
        viewModel.delete(item[position])
        item.removeAt(position)



        if(size==0)
            (context as Activity).findViewById<ConstraintLayout>(R.id.rl27).visibility=View.VISIBLE
        else
            (context as Activity).findViewById<ConstraintLayout>(R.id.rl27).visibility=View.GONE
//        (context as Activity).findViewById<MaterialCardView>(R.id.materialCardView25).setOnClickListener {
//            (context as Activity).startActivity(Intent(context, MainActivity::class.java))
//            finishAffinity(context)
//        }
        notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavCityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fav_city_single_row,parent,false)
        return FavCityViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavCityViewHolder, position: Int) {
        val currItem=item[position]
        (context as Activity).findViewById<LottieAnimationView>(R.id.progressBarHome).visibility=View.VISIBLE
        (context as Activity).findViewById<RecyclerView>(R.id.fav_city_recycler).visibility=View.GONE
//        (context as Activity).findViewById<RelativeLayout>(R.id.single_home).visibility=View.GONE

        WeatherTask(currItem.name,context,holder).execute()
        (context as Activity).findViewById<SwipeRefreshLayout>(R.id.refresh).setOnRefreshListener {
            if (ConnectionManager().checkConnectivity(context!!)) {

                (context as Activity).findViewById<LottieAnimationView>(R.id.progressBarHome).visibility =
                    View.VISIBLE
                (context as Activity).findViewById<RecyclerView>(R.id.fav_city_recycler).visibility =
                    View.GONE
                WeatherTask(currItem.name, context, holder).execute()
            }
            else
            {
                val alterDialog =
                    AlertDialog.Builder(context!!)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Internet Connection can't be establish!")
                alterDialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                    context.startActivity(settingsIntent)
                }

                alterDialog.setNegativeButton("Exit") { text, listener ->
                    finishAffinity(context!!)//closes all the instances of the app and the app closes completely
                }
                alterDialog.setCancelable(false)
                val alert: AlertDialog = alterDialog.create()
                alert.show()
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(context!!,R.drawable.dialog_bg))

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#2D3650"))

            }

        }
        holder.itemView.setOnClickListener {
            if (ConnectionManager().checkConnectivity(context!!)) {

                val intent = Intent(context, WeatherActivity::class.java)
                intent.putExtra("city_name", currItem.name)
                intent.putExtra("from", "fromFav")

                context.startActivity(intent)
            }
            else
            {
                val alterDialog =
                    AlertDialog.Builder(context!!)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Internet Connection can't be establish!")
                alterDialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                    context.startActivity(settingsIntent)
                }

                alterDialog.setNegativeButton("Exit") { text, listener ->
                    (context as Activity).finishAffinity()//closes all the instances of the app and the app closes completely
                }
                alterDialog.setCancelable(false)
                val alert: AlertDialog = alterDialog.create()
                alert.show()
                alert.setCanceledOnTouchOutside(true);
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(context!!,R.drawable.dialog_bg))

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#2D3650"))

            }
        }


    }

    override fun getItemCount(): Int {
        return item.size
    }
    class FavCityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val city: TextView =itemView.findViewById(R.id.fav_city)
        val updatedTime: TextView =itemView.findViewById(R.id.updatedTime)
        val fav_temp: TextView =itemView.findViewById(R.id.fav_temp)
        val background:RelativeLayout=itemView.findViewById<RelativeLayout>(R.id.single_home)


    }

     class WeatherTask(val city: String,val context:Context,val holder:FavCityViewHolder) : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()

        }


        override fun doInBackground(vararg p0: String?): String? {
            var response: String?

            var APPID = "e55a6c82e17579c3f5a238e30a32977f"
            if (ConnectionManager().checkConnectivity(context)) {

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
                val alterDialog = androidx.appcompat.app.AlertDialog.Builder(context as Context)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Internet Connection can't be establish!")
                alterDialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
//                    startActivity(settingsIntent)
                }

                alterDialog.setNegativeButton("Exit") { text, listener ->
                    finishAffinity(context as Activity)
                }
                alterDialog.setCancelable(false)

                alterDialog.create()
                alterDialog.show()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)


            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")

                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")

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

                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val windSpeed = wind.getString("speed")
                val sunset: Long = sys.getLong("sunset")
                val description = weather.getString("description")
                val address = jsonObj.getString("name")

                (context as Activity).findViewById<LottieAnimationView>(R.id.progressBarHome).visibility=View.GONE
                (context as Activity).findViewById<SwipeRefreshLayout>(R.id.refresh).isRefreshing=false
                (context as Activity).findViewById<RecyclerView>(R.id.fav_city_recycler).visibility=View.VISIBLE
//                (context as Activity).findViewById<RelativeLayout>(R.id.single_home).visibility=View.GONE
                val hrs:Int=SimpleDateFormat("hh", Locale.ENGLISH).format(Date(sunset * 1000)).toInt()
                val ampm:String=SimpleDateFormat("a", Locale.ENGLISH).format(Date(sunset * 1000))
                if(description.equals("snow") && hrs>=6&&ampm.equals("PM"))
                    holder.background.setBackgroundColor(Color.parseColor("#5C6BC0"))

                else if(description.equals("snow") &&ampm.equals("AM"))
                   holder.background.setBackgroundColor(Color.parseColor("#2196F3"))

                else if(description.contains("rain"))
                    holder.background.setBackgroundColor(Color.parseColor("#5C6BC0"))

                else if(description.contains("cloud")&&hrs>=6&&hrs<=12&&ampm.equals("PM"))
                    holder.background.setBackgroundColor(Color.parseColor("#7986CB"))

                else if((description.contains("clouds") &&ampm.equals("AM"))||description==("clear sky"))
                    holder.background.setBackgroundColor(Color.parseColor("#64B5F6"))

                else if(description.contains("overcast"))
                    holder.background.setBackgroundColor(Color.parseColor("#78909C"))

                else if(description.contains("fog")||description.contains("haze")||description.contains("smoke"))
                    holder.background.setBackgroundColor(Color.parseColor("#607D8B"))

                else if(description.contains("mist"))
                    holder.background.setBackgroundColor(Color.parseColor("#00BFA5"))
//
                else
                    holder.background.setBackgroundColor(Color.parseColor("#FFA726"))


                holder.city.text=city
                holder.updatedTime.text=updatedAtReq
                holder.fav_temp.text=temp
            } catch (e: Exception) {

            }
        }



    }


}