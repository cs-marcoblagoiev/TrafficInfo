package com.mmproductions.trafficinfo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Marco on 3/24/2018.
 */

public class AppController extends Application {
    private static AppController sInstance;
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static synchronized AppController getInstance() {
        return sInstance;
    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

}
