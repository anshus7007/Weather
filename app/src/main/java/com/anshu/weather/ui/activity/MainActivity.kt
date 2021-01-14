package com.anshu.weather.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.anshu.weather.R
//import com.anshu.weather.others.InitApplication
import com.anshu.weather.ui.fragments.SearchFragment
import com.anshu.weather.ui.fragments.SettingsFragment
import com.anshu.weather.util.ConnectionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity(),SettingsFragment.Listener {
//    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
//
//     var lat:Double= 0.0
//     var lon:Double=0.0
//
//    internal lateinit var mLocationRequest: LocationRequest
//    var PERMISSION_ID=52
    lateinit var fab:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent=intent
//        val instance=InitApplication()
//        if (instance.isNightModeEnabled() == true) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//        } else {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//        }
        val check=intent.getStringExtra("settings")
        println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!$check!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        fab=findViewById(R.id.fab)
//        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)
//        findViewById<RelativeLayout>(R.id.rl1_settings).setOnClickListener {
//            getLastLocation()


//
//        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps()
//        }
//  ////
////        btnStartupdate.setOnClickListener {
////            if (checkPermissionForLocation(this)) {
//                startLocationUpdates()
////                btnStartupdate.isEnabled = false
////                btnStopUpdates.isEnabled = true
////            }
////        }
////
////        btnStopUpdates.setOnClickListener {
////            stoplocationUpdates()
////            txtTime.text = "Updates Stoped"
////            btnStartupdate.isEnabled = true
////            btnStopUpdates.isEnabled = false
////        }
        fab.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this!!)) {

            val i = Intent(this, HomeActivity::class.java)
            overridePendingTransition(
                android.R.anim.accelerate_decelerate_interpolator,
                android.R.anim.accelerate_decelerate_interpolator
            )
            startActivity(i)
        }
        else
        {
            val alterDialog =
                AlertDialog.Builder(this!!)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this!!)//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)
            val alert: AlertDialog = alterDialog.create()
            alert.show()
            alert.setCanceledOnTouchOutside(true);
            alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(this!!,R.drawable.dialog_bg))

            alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
            alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))
            alert.getButton(AlertDialog.BUTTON_NEUTRAL).setBackgroundColor(Color.parseColor("#2D3650"))

        }

    }
        bottomNavigationView.background=null
        bottomNavigationView.menu.getItem(1).isEnabled=false
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(R.id.flFragment, SearchFragment())
            .commit()

        if(check.equals("true"))
        {
            supportFragmentManager.beginTransaction().replace(R.id.flFragment, SettingsFragment())
                .commit()
            val menu: Menu = bottomNavigationView.menu
            menu.findItem(R.id.Settings).isChecked=true
//            println("111111111111111111111${menu.getItem(R.id.Search)}1111111111111111111111")
//            menu.getItem(R.id.Settings).setChecked(true)
//            bottomNavigationView.itemIconTintList=ColorStateList.valueOf(Color.parseColor("#64dd17"))
//
//            bottomNavigationView.itemTextColor= ColorStateList.valueOf(Color.parseColor("#64dd17"))


        }
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
    override fun onBackPressed() {
        ActivityCompat.finishAffinity(this!!)//closes all the instances of the app and the app closes completely
    }
    override fun onThemeChanged() {
        val intent = intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        finish()
        startActivity(intent)
    }
//    fun getLastLocation()
//    {
//        if(checkPermission())
//        {
//            if(isLocationEnabled())
//            {
//                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
//                    var location=task.result
//                    if(location==null)
//                    {
//                        getNewLocation()
//                    }
//                    else
//                    {
//
//                        lat=location.latitude
//                        lon=location.longitude
//                        getCityName(lat,lon)
//                    }
//                }
//            }
//            else
//            {
//                    Toast.makeText(this,"Please enable ur loaction service",Toast.LENGTH_LONG).show()
//            }
//        }
//        else
//
//        {
//            requestPermission()
//        }
//    }
//    fun getNewLocation()
//    {
//        mLocationRequest=LocationRequest()
//        mLocationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval=0
//        mLocationRequest.fastestInterval=0
//        mLocationRequest.numUpdates=2
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationProviderClient.requestLocationUpdates(
//            mLocationRequest,locationCallback,Looper.myLooper()
//        )
//    }
//    private val locationCallback=object:LocationCallback()
//    {
//        override fun onLocationResult(p0: LocationResult?) {
//            var lastLocation=p0?.lastLocation
//            lat= lastLocation?.latitude!!
//            lon=lastLocation!!.longitude
//        }
//    }
//    fun checkPermission():Boolean{
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//        {
//            return true
//        }
//        return false
//    }
//    fun requestPermission()
//    {
//        ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION),PERMISSION_ID)
//    }
//
//    fun isLocationEnabled():Boolean
//    {
//        var locationManager=getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                startLocationUpdates()
////                btnStartupdate.isEnabled = false
////                btnStopUpdates.isEnabled = true
//            } else {
//                goToSettings()
//
//            }
//            }
//        }
//
//    private fun getCityName(lat:Double,lon:Double):String{
//        var cityName=""
//        var geoCoder= Geocoder(this,Locale.getDefault())
//        var address=geoCoder.getFromLocation(lat,lon,1)
//        cityName= address.get(0).locality
//        Toast.makeText(this@MainActivity, "^^^^^^^^^^^^^^$cityName^^^^^^^^^^^^^^^", Toast.LENGTH_LONG).show()
//        return cityName
//    }

//
//    private fun buildAlertMessageNoGps() {
//
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//            .setCancelable(false)
//            .setPositiveButton("Yes") { dialog, id ->
//                startActivityForResult(
//                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                    , 11
//                )
//            }
//            .setNegativeButton("No") { dialog, id ->
//                dialog.cancel()
//                finish()
//            }
//        val alert: AlertDialog = builder.create()
//        alert.show()
//
//
//    }
//
//
////    protected fun startLocationUpdates() {
////
////        // Create the location request to start receiving updates
////
////        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
////        mLocationRequest!!.setInterval(INTERVAL)
////        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)
////
////        // Create LocationSettingsRequest object using location request
////        val builder = LocationSettingsRequest.Builder()
////        builder.addLocationRequest(mLocationRequest!!)
////        val locationSettingsRequest = builder.build()
////
////        val settingsClient = LocationServices.getSettingsClient(this)
////        settingsClient.checkLocationSettings(locationSettingsRequest)
////
////        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
////        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
////        if (ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                this,
////                Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////
////
////            return
////        }
////        mFusedLocationProviderClient!!.requestLocationUpdates(
////            mLocationRequest, mLocationCallback,
////            Looper.myLooper()
////        )
////    }
////
////    private val mLocationCallback = object : LocationCallback() {
////        override fun onLocationResult(locationResult: LocationResult) {
////            // do work here
////            locationResult.lastLocation
////            onLocationChanged(locationResult.lastLocation)
////        }
////    }
////
////    fun onLocationChanged(location: Location) {
////        // New location has now been determined
////
////        mLastLocation = location
////        val date: Date = Calendar.getInstance().time
////        val sdf = SimpleDateFormat("hh:mm:ss a")
////        txtTime.text = "Updated at : " + sdf.format(date)
////        txtLat.text = "LATITUDE : " + mLastLocation.latitude
////        txtLong.text = "LONGITUDE : " + mLastLocation.longitude
////        // You can now create a LatLng Object for use with maps
////    }
////
////    private fun stoplocationUpdates() {
////        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
////    }
////
////
////
//    fun checkPermissionForLocation(context: Context): Boolean {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
//                PackageManager.PERMISSION_GRANTED
//            ) {
//                true
//            } else {
//                // Show the permission request
//                ActivityCompat.requestPermissions(
//                    this@MainActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
//                    PERMISSION_ID
//                )
//                false
//            }
//        } else {
//            true
//        }
//    }
////
//    private fun goToSettings() {
//        val myAppSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//        myAppSettings.data = uri
//        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
//        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//        startActivityForResult(myAppSettings, PERMISSION_ID)
//        Toast.makeText(this@MainActivity, "Go to Permissions and grant permissions.", Toast.LENGTH_LONG).show()
//    }
//
}