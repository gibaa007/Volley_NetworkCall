package com.nagainfo.myapplication;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by nagainfo on 30/3/16.
 */
public class AppController extends Application {
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    static AppController con;

    @Override
    public void onCreate() {
        super.onCreate();
         con = this;
    }
    public static synchronized AppController getInstance(MainActivity mainActivity) {
        return con;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        /*if (imageLoader == null) {
            imageLoader = new ImageLoader(this.imageLoader,
                    new LruBitmapCache());
        }*/
        return this.imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {

        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
}
