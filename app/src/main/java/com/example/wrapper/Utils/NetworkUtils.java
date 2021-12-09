package com.example.wrapper.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Md.harun or rashid on 08,December,2021
 * BABL, Bangladesh,
 */
public class NetworkUtils {

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return 1;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return 2;
        }
        return 0;
    }


    public static URL buildURL(String link) {

        Uri builtUri = Uri.parse(link).buildUpon().build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.d("BUILT_URL", "Built URI=" + url);
        return url;
    }


}
