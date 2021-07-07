package com.example.RestaurantManagement;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import static androidx.core.content.ContextCompat.getSystemService;

public class Support {

    static public boolean checkConnect(Activity activity)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo((ConnectivityManager.TYPE_MOBILE));
        if (wifiConn != null && wifiConn.isConnected() || mobileConn != null && mobileConn.isConnected())
        {
            return  true;
        }
        else
        {
            return  false;
        }
    }

}
