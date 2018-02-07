package com.mmproductions.trafficinfo

import android.content.Context
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * Created by Marco on 2/7/2018.
 */

class ApiCalls {
    fun getTomTomdata(mLat: String, mLon: String, context: Context, trafficData: TextView) {
        val url = "https://api.tomtom.com/traffic/services/4/flowSegmentData/absolute/10/json?key=uNXbMuwElInZECgC1f1b0kpxSA5yUHe5&unit=KMPH&point=$mLat,$mLon"

        val jsObjRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            val flowSegmentData: JSONObject = response.get("flowSegmentData") as JSONObject
            val freeFlowTravelTime = flowSegmentData.get("freeFlowTravelTime").toString().toDouble()
            val currentTravelTime = flowSegmentData.get("currentTravelTime").toString().toDouble()
            val currentTraffic = currentTravelTime / freeFlowTravelTime
            trafficData.text = "Current traffic: " + currentTraffic
        }, Response.ErrorListener {
            // TODO Auto-generated method stub
        })

        val queue = Volley.newRequestQueue(context)
        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest)
    }

    fun getBreezometerData(mLat: String, mLon: String, context: Context, breezometerData: TextView) {
        val url = "https://api.breezometer.com/baqi/?lat=$mLat&lon=$mLon&fields=dominant_pollutant_canonical_name,breezometer_aqi&key=b1d782856d2a431db457fecf4bc32e26"

        val jsObjRequest = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { response ->
            val aqi = response.get("breezometer_aqi")
            val pollutant = response.get("dominant_pollutant_canonical_name")
            breezometerData.text = "Polution level: " + aqi + "\nPredominant pollutant: " + pollutant
        }, Response.ErrorListener {
            // TODO Auto-generated method stub
        })

        val queue = Volley.newRequestQueue(context)
        // Access the RequestQueue through your singleton class.
        queue.add(jsObjRequest)
    }
}