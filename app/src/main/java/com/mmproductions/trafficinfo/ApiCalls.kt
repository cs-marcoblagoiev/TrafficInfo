package com.mmproductions.trafficinfo

import android.content.Context
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONObject





/**
 * Created by Marco on 2/7/2018.
 */

class ApiCalls {
    var queue : RequestQueue? = null
    fun getTomTomdata(mLat: String, mLon: String, context: Context, trafficData: TextView, activity: MainActivityKotlin) {
        val url = "https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?key=uNXbMuwElInZECgC1f1b0kpxSA5yUHe5&unit=KMPH&point=$mLat,$mLon"

        val jsObjRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            val flowSegmentData: JSONObject = response.get("flowSegmentData") as JSONObject
            val freeFlowTravelTime = flowSegmentData.get("freeFlowTravelTime").toString().toDouble()
            val currentTravelTime = flowSegmentData.get("currentTravelTime").toString().toDouble()
            val currentTraffic = currentTravelTime / freeFlowTravelTime
            trafficData.text = "Current traffic: " + currentTraffic

            activity.trafficQuantity = currentTraffic
        }, Response.ErrorListener {
            // TODO Auto-generated method stub
        })

        if (queue == null) {
            queue = Volley.newRequestQueue(context)
        }
        // Access the RequestQueue through your singleton class.
        queue?.add(jsObjRequest)
    }

    fun getBreezometerData(mLat: String, mLon: String, context: Context, breezometerData: TextView, activity: MainActivityKotlin) {
        val url = "https://api.breezometer.com/baqi/?lat=$mLat&lon=$mLon&fields=dominant_pollutant_canonical_name,breezometer_aqi&key=3ab12452fdee438e9d6fdd95544d989f"

        val jsObjRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            val aqi = (response.get("breezometer_aqi") as Int).toDouble()
            val pollutant = response.get("dominant_pollutant_canonical_name")
            breezometerData.text = "Polution level: " + aqi + "\nPredominant pollutant: " + pollutant

            activity.airQuality = aqi
        }, Response.ErrorListener {
            // TODO Auto-generated method stub
        })

        val queue = Volley.newRequestQueue(context)
        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest)
    }

    fun uploadToFirestore(timestamp: String, mLat: String, mLon: String, currentTraffic: String,
                          aqi: String, indoors: Boolean) {
        val db = FirebaseFirestore.getInstance()
        // Create a new user with a first and last name
        val data = HashMap<String, Any>()
        data.put("time", timestamp)
        data.put("latitude", mLat)
        data.put("longitude", mLon)
        data.put("traffic", currentTraffic)
        data.put("airQuality", aqi)
        data.put("indoors", indoors)

        var id = timestamp.replace(" ", "_")
        id = id.replace("/", "_")


        // Add a new document with a generated ID
        db.collection("data")
                .document(id)
                .set(data)
                .addOnSuccessListener(OnSuccessListener { documentReference -> Log.d("Tag", "DocumentSnapshot added ") })
                .addOnFailureListener(OnFailureListener { e -> Log.w("Tag", "Error adding document", e) })

    }
}