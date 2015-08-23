
package com.example.sthienpont.weatherlight.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.example.sthienpont.weatherlight.CallWeatherAPI;
import com.example.sthienpont.weatherlight.dataobjects.Preferences;
import com.example.sthienpont.weatherlight.utils.PreferenceHelper;

/**
 * Created by Sébastien on 23/8/15.
 */
public class WeatherReceiver extends BroadcastReceiver {
    private static final String TAG = "WeatherReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "Executing task");
        Preferences preferences = PreferenceHelper.loadPreferences(context);
        if (!preferences.currentCity.isEmpty()) {
            new CallWeatherAPI(new TextView(context), context, false).execute();
        }
    }
}
