package com.anshu.weather

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    lateinit var fab:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        fab=findViewById(R.id.fab)
        fab.setOnClickListener{
            val i = Intent(this, HomeActivity::class.java)
            overridePendingTransition(
                android.R.anim.accelerate_decelerate_interpolator,
                android.R.anim.accelerate_decelerate_interpolator
            )
            startActivity(i)
        }
        bottomNavigationView.background=null
        bottomNavigationView.menu.getItem(1).isEnabled=false
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.flFragment, SearchFragment())
            .commit()

    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectFragment: Fragment? = null
            when (item.itemId) {
                R.id.Search -> selectFragment = SearchFragment()
                R.id.Settings -> selectFragment = SettingsFragment()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.flFragment,
                selectFragment!!
            ).commit()
            true
        }

}