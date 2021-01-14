package com.anshu.weather.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.anshu.weather.R
import com.anshu.weather.adapter.FavCityAdapter
import com.anshu.weather.db.db2_for_fav_city.FavCityDB
import com.anshu.weather.db.db2_for_fav_city.entity.FavCity
import com.anshu.weather.fav_city_view_model.FavCityViewModel
import com.anshu.weather.fav_city_view_model.FavCityViewModelFactory
import com.anshu.weather.others.Constants
import com.anshu.weather.others.SwipeToDeleteCallback
import com.anshu.weather.repositories.FavCityRepository
import com.anshu.weather.ui.fragments.SearchFragment
import com.anshu.weather.util.ConnectionManager
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    lateinit var fav_city_recycler:RecyclerView
    lateinit var fav_city_adapter:FavCityAdapter
    lateinit var no_fav_city:ConstraintLayout
    lateinit var add_fav_city:MaterialCardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fav_city_recycler=findViewById(R.id.fav_city_recycler)
        no_fav_city=findViewById(R.id.rl27)
//        add_fav_city=findViewById(R.id.materialCardView25)
        val database= FavCityDB(this)
        val repository= FavCityRepository(database)
        val factory= FavCityViewModelFactory(repository)
        val viewmodel = ViewModelProviders.of(this,factory).get(FavCityViewModel::class.java)
        setSupportActionBar(findViewById(R.id.toolBarHome))
        supportActionBar?.title = "Favourites"

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        fav_city_recycler.visibility=View.VISIBLE
//        findViewById<RelativeLayout>(R.id.single_home).visibility=View.VISIBLE


        fav_city_adapter= FavCityAdapter(this,0, mutableListOf(),viewmodel)
        val layoutManager= LinearLayoutManager(this)
        fav_city_recycler.layoutManager = layoutManager
        fav_city_recycler.adapter = fav_city_adapter
        viewmodel.getAllFavCity().observe(this!!, Observer {
            fav_city_adapter.item=it
            if(it.size==0)
                findViewById<ConstraintLayout>(R.id.rl27).visibility=View.VISIBLE
            else
                findViewById<ConstraintLayout>(R.id.rl27).visibility=View.GONE
            fav_city_adapter.size=it.size
            fav_city_adapter.notifyDataSetChanged()
        })


        if(fav_city_adapter.itemCount ==0)
            no_fav_city.visibility= View.VISIBLE
        else
            no_fav_city.visibility= View.GONE

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (ConnectionManager().checkConnectivity(applicationContext!!)) {

                    val adapter = fav_city_recycler.adapter as FavCityAdapter
                    adapter.removeAt(viewHolder.adapterPosition)
                }
                else
                {
                    val alterDialog =
                        AlertDialog.Builder(applicationContext!!)
                    alterDialog.setTitle("No Internet")
                    alterDialog.setMessage("Internet Connection can't be establish!")
                    alterDialog.setPositiveButton("Open Settings") { text, listener ->
                        val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                        startActivity(settingsIntent)
                    }

                    alterDialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this@HomeActivity)//closes all the instances of the app and the app closes completely
                    }
                    alterDialog.setCancelable(false)
                    val alert: AlertDialog = alterDialog.create()
                    alert.show()
                    alert.setCanceledOnTouchOutside(true);
                    alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(applicationContext!!,R.drawable.dialog_bg))

                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
                    alert.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#2D3650"))

                }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(fav_city_recycler)
//        add_fav_city.setOnClickListener {
//            startActivity(Intent(this,MainActivity::class.java))
//            finish()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id=item.itemId

        if(id==android.R.id.home){//home id is the id of the button on the tool bar
            val intent=(Intent(this,MainActivity::class.java))
            startActivity(intent)
            finish()
        //start he drawer from the start
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}