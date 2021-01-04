package com.anshu.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.flFragment, HomeFragment())
            .commit()

    }

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectFragment: Fragment? = null
            when (item.itemId) {
                R.id.Home -> selectFragment = HomeFragment()
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