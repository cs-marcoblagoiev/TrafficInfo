package com.mmproductions.trafficinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

/**
 * Created by Marco on 2/4/2018.
 */
class MainActivityKotlin : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLat: String? = null
    private var mLon: String? = null
    private var service: ApiCalls? = null
    private var trafficData: TextView? = null
    private var breezometerData: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = ApiCalls()

        trafficData = findViewById(R.id.trafficData)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationTextView = findViewById<View>(R.id.gpsLocation) as TextView

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mFusedLocationClient!!.lastLocation
                .addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d("hello", "Location: " + location.toString())
                        mLat = java.lang.Double.toString(location.latitude)
                        mLon = java.lang.Double.toString(location.longitude)
                        locationTextView.text = mLat + ", " + mLon

                    }
                }
    }


    fun onBreezometerButtonPressed(view: View) {
        service?.getBreezometerData(mLat!!, mLon!!, this, breezometerData!!)
    }

    fun onTrafficButtonPressed(view: View) {
        service?.getTomTomdata(mLat!!, mLon!!, this, trafficData!!)
    }
}