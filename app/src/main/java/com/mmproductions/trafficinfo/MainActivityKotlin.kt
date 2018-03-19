package com.mmproductions.trafficinfo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Thread.sleep
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Marco on 2/4/2018.
 */
class MainActivityKotlin : AppCompatActivity() {

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLat: String = "0.0"
    private var mLon: String = "0.0"
    public var trafficQuantity: Double = 0.0
    public var airQuality: Double = 0.0
    private var service: ApiCalls? = null
    private var trafficData: TextView? = null
    private var breezometerData: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        service = ApiCalls()

        trafficData = findViewById(R.id.trafficData)
        breezometerData = findViewById(R.id.breezometerData)

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
        mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        Log.d("hello", "Location: " + location.toString())
                        mLat = java.lang.Double.toString(location.latitude)
                        mLon = java.lang.Double.toString(location.longitude)
                        locationTextView.text = mLat + ", " + mLon

                    }
                }


        //Declare the timer
        val t = Timer()
        //Set the schedule function and rate
        t.scheduleAtFixedRate(object : TimerTask() {

            override fun run() {
                //Called each time when 1000 milliseconds (1 second) (the period parameter)
                onBreezometerButtonPressed(findViewById(R.id.breezometerData))
                onTrafficButtonPressed(findViewById(R.id.breezometerData))

                sleep(5000)


                onFirestoreButtonPressed(findViewById(R.id.breezometerData))
            }

        },
                //Set how long before to start calling the TimerTask (in milliseconds)
                10000,
                //Set the amount of time between each execution (in milliseconds)
                60000)

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    fun onBreezometerButtonPressed(view: View) {
        service?.getBreezometerData(mLat, mLon, this, breezometerData!!, this)
    }

    fun onTrafficButtonPressed(view: View) {
        service?.getTomTomdata(mLat, mLon, this, trafficData!!, this)
    }

    fun onFirestoreButtonPressed(view: View) {
        val cal = java.util.Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val indoors = findViewById<RadioButton>(R.id.indoors).isChecked

        service?.uploadToFirestore(sdf.format(cal.getTime()), mLat, mLon, trafficQuantity.toString(),
                airQuality.toString(), indoors);
    }
}