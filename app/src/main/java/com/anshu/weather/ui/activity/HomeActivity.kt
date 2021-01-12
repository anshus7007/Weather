package com.anshu.weather.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.google.android.material.card.MaterialCardView

class HomeActivity : AppCompatActivity() {
    lateinit var fav_city_recycler:RecyclerView
    lateinit var fav_city_adapter:FavCityAdapter
    lateinit var no_fav_city:RelativeLayout
    lateinit var add_fav_city:MaterialCardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        fav_city_recycler=findViewById(R.id.fav_city_recycler)
        no_fav_city=findViewById(R.id.rl20)
        add_fav_city=findViewById(R.id.materialCardView1)
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
        add_fav_city.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        fav_city_adapter= FavCityAdapter(this,0, mutableListOf(),viewmodel)
        val layoutManager= LinearLayoutManager(this)
        fav_city_recycler.layoutManager = layoutManager
        fav_city_recycler.adapter = fav_city_adapter
        viewmodel.getAllFavCity().observe(this!!, Observer {
            fav_city_adapter.item=it
            if(it.size==0)
                findViewById<RelativeLayout>(R.id.rl20).visibility=View.VISIBLE
            else
                findViewById<RelativeLayout>(R.id.rl20).visibility=View.GONE
            fav_city_adapter.size=it.size
            fav_city_adapter.notifyDataSetChanged()
        })


        if(fav_city_adapter.itemCount ==0)
            no_fav_city.visibility= View.VISIBLE
        else
            no_fav_city.visibility= View.GONE

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fav_city_recycler.adapter as FavCityAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(fav_city_recycler)
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