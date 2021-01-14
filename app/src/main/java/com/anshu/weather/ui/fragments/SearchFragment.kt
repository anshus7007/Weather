package com.anshu.weather.ui.fragments

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.getDrawable
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.anshu.weather.R
import com.anshu.weather.adapter.CityWeatherAdapter
import com.anshu.weather.city_view_model.CityViewModel
import com.anshu.weather.city_view_model.CityViewModelFactory
import com.anshu.weather.db.db1_for_city_name.CityDatabase
import com.anshu.weather.db.db1_for_city_name.entity.CityWeather
import com.anshu.weather.others.Constants
import com.anshu.weather.repositories.CityRepository
import com.anshu.weather.ui.activity.WeatherActivity
import com.anshu.weather.util.ConnectionManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment() {

    lateinit var  search:EditText
    lateinit var  txtSearch:TextView
    lateinit var no_results_found:TextView
    var results:String="City found"
    lateinit var searchList:RecyclerView
//    val userInfoList = mutableListOf<CityWeather>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_search, container, false)
         search=view.findViewById(R.id.etSearch)
        txtSearch=view.findViewById(R.id.txtSearch)
        searchList=view.findViewById(R.id.searchList)
//        val db = Room.databaseBuilder(activity!!, CityDatabase::class.java, "cityDB.db").allowMainThreadQueries().build()

        val database= CityDatabase(activity!!)
        val repository= CityRepository(database)
        val factory= CityViewModelFactory(repository)
        val viewmodel = ViewModelProviders.of(this, factory).get(CityViewModel::class.java)

        no_results_found=view.findViewById(R.id.no_results)
        Constants.cityList.clear()
        val adapter= CityWeatherAdapter(activity as Context, listOf(), viewmodel)
        var layoutManager = FlexboxLayoutManager(activity)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        searchList.layoutManager = layoutManager

        searchList.adapter = adapter

        txtSearch.setOnClickListener{
            if (ConnectionManager().checkConnectivity(activity!!)) {
                println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$")
                search(search, viewmodel)
            }
            else
            {
                val alterDialog =
                    AlertDialog.Builder(activity!!)
                alterDialog.setTitle("No Internet")
                alterDialog.setMessage("Internet Connection can't be establish!")
                alterDialog.setPositiveButton("Open Settings") { text, listener ->
                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                    startActivity(settingsIntent)
                }

                alterDialog.setNegativeButton("Exit") { text, listener ->
                    finishAffinity(activity!!)//closes all the instances of the app and the app closes completely
                }
                alterDialog.setCancelable(false)
                val alert: AlertDialog = alterDialog.create()
                alert.show()
                alert.setCanceledOnTouchOutside(true);
                alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.dialog_bg))

                alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
                alert.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#2D3650"))

            }
        }
        viewmodel.getAllSearchedCity().observe(activity!!, Observer {
            adapter.items = it
            adapter.notifyDataSetChanged()
        })




        return  view
    }
    fun search(search: EditText, viewModel: CityViewModel) {

        val city = search.text.toString()
        if (!city.isEmpty()) {
//            val cityEntity = CityWeather(
//                city)
//
//            if (!Constants.cityList.contains(CityWeather(city)))
//            {
//                viewModel.insert(cityEntity)
//                Constants.cityList.add(CityWeather(city))
//
////             result = context?.let { DBAsyncTask(it, cityEntity, 1).execute().get() } == true
//            }
//        if(result == true){
//
//
////            Toast.makeText(context,"Searched",Toast.LENGTH_SHORT).show()
//
//        }else{
//
//            Toast.makeText(context,"Some error occured",Toast.LENGTH_SHORT).show()
//
//        }
            no_results_found.visibility = View.GONE
            val intent = Intent(activity, WeatherActivity::class.java)
            intent.putExtra("city_name", city)
            intent.putExtra("from", "fromSearch")

            activity?.startActivity(intent)
        }
        else
        {
            view?.findViewById<FloatingActionButton>(R.id.fab)?.visibility=View.GONE
            val snackbar = view?.let {
                Snackbar
                    .make(it.findViewById(R.id.fl1), "Nothing to search", Snackbar.LENGTH_LONG)
            }
            if (snackbar != null) {
                snackbar.show()
            }
        }

    }

//    class DBAsyncTask(val context: Context, val restaurantEntity: CityWeather, val mode: Int): AsyncTask<Void, Void, Boolean>() {
//
//        val db = Room.databaseBuilder(context, CityDatabase::class.java, "cityDB.db").build()
//        override fun doInBackground(vararg params: Void?): Boolean {
//            when (mode) {
//
//                1 -> {
//
//                    db.getCityDao().upsert(restaurantEntity)
//                    db.close()
//                    return true
//
//                }
//                2 -> {
//                    db.getCityDao().delete(restaurantEntity)
//                    db.close()
//                    return true
//
//                }
//            }
//            return false
//        }
//
//
//    }
//        else
//        {
//            no_results_found.visibility=View.VISIBLE
//        }
    }

    //    inner class checkTask(val city: String) : AsyncTask<String, Void, String>() {
    //        override fun onPreExecute() {
    //            super.onPreExecute()
    //
    //        }
    //
    //
    //        override fun doInBackground(vararg p0: String?): String? {
    //            var response: String?
    //
    //            var APPID = "e55a6c82e17579c3f5a238e30a32977f"
    //            if (ConnectionManager().checkConnectivity(activity as Context)) {
    //
    //                try {
    //
    //                    response =
    //                            URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$APPID").readText(
    //                                    Charsets.UTF_8
    //                            )
    //                } catch (e: Exception) {
    //                    response = null
    //                }
    //                return response
    //
    //            }
    //            else {
    //                val alterDialog = androidx.appcompat.app.AlertDialog.Builder(activity as Context)
    //                alterDialog.setTitle("No Internet")
    //                alterDialog.setMessage("Internet Connection can't be established!")
    //                alterDialog.setPositiveButton("Open Settings") { text, listener ->
    //                    val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
    //                    startActivity(settingsIntent)
    //                }
    //
    //                alterDialog.setNegativeButton("Exit") { text, listener ->
    //                    System.exit(0)//closes all the instances of the app and the app closes completely
    //                }
    //                alterDialog.setCancelable(false)
    //
    //                alterDialog.create()
    //                alterDialog.show()
    //            }
    //            return null
    //        }
    //
    //        override fun onPostExecute(result: String?) {
    //            super.onPostExecute(result)
    //
    //            println("Heloooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo")
    //            try {
    //                val jsonObj = JSONObject(result)
    //                println(result)
    //                val message=jsonObj.getString("message")
    //                val cod=jsonObj.getString("cod")
    //                println("***********$message*******")
    //                if(message == "city not found" || cod == "404")
    //                    results="city not found"
    //            } catch (e: Exception) {
    //                results="city not found"
    //                Toast.makeText(activity,"********************$e",Toast.LENGTH_LONG).show()
    //            }
//            }
    //
    //
    //
//        }

    //}