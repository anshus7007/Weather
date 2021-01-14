package com.anshu.weather.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.anshu.weather.BuildConfig
import com.anshu.weather.R
import com.anshu.weather.ui.activity.CustomDialogFragment
import com.anshu.weather.ui.activity.MainActivity
import com.anshu.weather.ui.activity.WeatherActivityForMyLocation
import com.anshu.weather.util.ConnectionManager
import com.google.android.gms.location.*
import java.util.*


class SettingsFragment : Fragment() {
    lateinit var location:RelativeLayout
    lateinit var dark_mode: Switch
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val listener: Listener? = null
//internal lateinit var prefs:InitApplication
    var lat:Double= 0.0
    var lon:Double=0.0
     var city:String=""
    internal lateinit var mLocationRequest: LocationRequest
    var PERMISSION_ID=52
    lateinit var weather_channel:RelativeLayout
    lateinit var rate_us:RelativeLayout
    lateinit var share:RelativeLayout
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_settings, container, false)
//        prefs=InitApplication(activity!!)

        location=view.findViewById(R.id.rl1_settings)
        dark_mode=view.findViewById(R.id.dark_mode_switch)
        weather_channel=view.findViewById(R.id.rl5_settings)
        rate_us=view.findViewById(R.id.rl3_settings)
        share=view.findViewById(R.id.rl4_settings)
//        val instance=InitApplication()
//        if (instance.isNightModeEnabled()) {
//            dark_mode.setChecked(true)
//        }
//        dark_mode.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
//            instance.setIsNightModeEnabled(isChecked)
//            listener?.onThemeChanged()
//        })
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(activity!!)
//
//        findViewById<RelativeLayout>(R.id.rl1_settings).setOnClickListener {
        location.setOnClickListener {


                val locationManager =
                    activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps()
                } else {
                    if (ConnectionManager().checkConnectivity(activity!!)) {

                        city = getLastLocation()
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
                            ActivityCompat.finishAffinity(activity!!)//closes all the instances of the app and the app closes completely
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
            }


        rate_us.setOnClickListener {
            var dialog=CustomDialogFragment()
            dialog.show(childFragmentManager, "Custom")
        }


        weather_channel.setOnClickListener {
            val uri: Uri = Uri.parse("https://weather.com/en-IN/weather/today/l/INXX0096:1:IN?par=apple_widget&units=m")
            val i = Intent(Intent.ACTION_VIEW, uri)
            activity!!.startActivity(i)
        }

        share.setOnClickListener {
            shareApp()
        }
        return view
    }
    internal interface Listener {
        fun onThemeChanged()
    }
    fun shareApp()
    {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name")
            var shareMessage = "Download Weather App on Playstore:\n"
//            shareMessage = shareMessage + shareLink
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share app: "))
        } catch (e: Exception) {
        }
    }
    private fun getLastLocation():String
    {
        var cityName=""
        if(checkPermission())
        {
            if(isLocationEnabled())
            {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task->
                    var location=task.result
                    if(location==null)
                    {
                        getNewLocation()
                    }
                    else
                    {

                        lat=location.latitude
                        lon=location.longitude
                        cityName= getCityName(lat, lon)
                        val intent = Intent(context, WeatherActivityForMyLocation::class.java)
                        intent.putExtra("city_name", cityName)
//                intent.putExtra("from", "fromSearch")
//                        activity!!.overridePendingTransition(
//                            android.R.anim.slide_out_right,
//                            android.R.anim.slide_in_left
//                        )
                        activity!!.startActivity(intent)
                    }

                }
                println("%555563666666666666666666555${cityName}444444444444444444444444444")

                return cityName

            }
            else
            {
                buildAlertMessageNoGps()

                Toast.makeText(activity, "Please enable ur loaction service", Toast.LENGTH_LONG).show()
            }
        }
        else

        {
            requestPermission()
        }
        return cityName
    }
    fun getNewLocation()
    {
        mLocationRequest=LocationRequest()
        mLocationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval=0
        mLocationRequest.fastestInterval=0
        mLocationRequest.numUpdates=2
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest, locationCallback, Looper.myLooper()
        )
    }
    private val locationCallback=object: LocationCallback()
    {
        override fun onLocationResult(p0: LocationResult?) {
            var lastLocation=p0?.lastLocation
            lat= lastLocation?.latitude!!
            lon=lastLocation!!.longitude
            city=getCityName(lat, lon)
        }
    }
    fun checkPermission():Boolean{
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
        {
            return true
        }
        return false
    }
    fun requestPermission()
    {
        ActivityCompat.requestPermissions(
            activity!!, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean
    {
        var locationManager=activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            buildAlertMessageNoGps()
//        }
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                startLocationUpdates()
//                btnStartupdate.isEnabled = false
//                btnStopUpdates.isEnabled = true
            } else {
                goToSettings()

            }
        }
    }

    private fun getCityName(lat: Double, lon: Double):String {
        var cityName=""
        var geoCoder= Geocoder(activity!!, Locale.getDefault())
        var address=geoCoder.getFromLocation(lat, lon, 1)
        cityName= address.get(0).locality
        city=cityName
        return cityName
    }

    private fun buildAlertMessageNoGps() {

        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
            .setCancelable(false)

            .setPositiveButton("Yes") { dialog, id ->

                startActivityForResult(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 11
                )
            }
            .setNegativeButton("No") { dialog, id ->
                dialog.cancel()
            }
        val alert: AlertDialog = builder.create()
        alert.show()
        alert.setCanceledOnTouchOutside(true);
        alert.getWindow()?.setBackgroundDrawable(ContextCompat.getDrawable(activity!!,R.drawable.dialog_bg))

        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FFFFFF"))
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FFFFFF"))



    }


    //    protected fun startLocationUpdates() {
//
//        // Create the location request to start receiving updates
//
//        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest!!.setInterval(INTERVAL)
//        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)
//
//        // Create LocationSettingsRequest object using location request
//        val builder = LocationSettingsRequest.Builder()
//        builder.addLocationRequest(mLocationRequest!!)
//        val locationSettingsRequest = builder.build()
//
//        val settingsClient = LocationServices.getSettingsClient(this)
//        settingsClient.checkLocationSettings(locationSettingsRequest)
//
//        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//
//            return
//        }
//        mFusedLocationProviderClient!!.requestLocationUpdates(
//            mLocationRequest, mLocationCallback,
//            Looper.myLooper()
//        )
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            // do work here
//            locationResult.lastLocation
//            onLocationChanged(locationResult.lastLocation)
//        }
//    }
//
//    fun onLocationChanged(location: Location) {
//        // New location has now been determined
//
//        mLastLocation = location
//        val date: Date = Calendar.getInstance().time
//        val sdf = SimpleDateFormat("hh:mm:ss a")
//        txtTime.text = "Updated at : " + sdf.format(date)
//        txtLat.text = "LATITUDE : " + mLastLocation.latitude
//        txtLong.text = "LONGITUDE : " + mLastLocation.longitude
//        // You can now create a LatLng Object for use with maps
//    }
//
//    private fun stoplocationUpdates() {
//        mFusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback)
//    }
//
//
//
    fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                // Show the permission request
                ActivityCompat.requestPermissions(
                    activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_ID
                )
                false
            }
        } else {
            true
        }
    }
    //
    private fun goToSettings() {
        val myAppSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
        myAppSettings.data = uri
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivityForResult(myAppSettings, PERMISSION_ID)
        Toast.makeText(activity, "Go to Permissions and grant permissions.", Toast.LENGTH_LONG).show()
    }


}