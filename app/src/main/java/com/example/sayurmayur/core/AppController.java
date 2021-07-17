package com.example.sayurmayur.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AppController {
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static boolean isInternetAvailable() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            return !address.equals("");
        } catch (UnknownHostException e) {
            // Log error
            e.printStackTrace();
        }
        return false;
    }

    public static void showMessage(Context context, String message, int duration){
        Toast.makeText(context, message, duration).show();
    }
}
