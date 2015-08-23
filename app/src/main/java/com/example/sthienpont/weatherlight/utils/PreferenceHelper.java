
package com.example.sthienpont.weatherlight.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.sthienpont.weatherlight.dataobjects.Preferences;
import com.google.gson.Gson;

/**
 * Created by Sébastien on 23/8/15.
 */
public class PreferenceHelper {
    private static final String TAG = "PreferenceHelper";
    private static final String USER_DATA = "userData";

    public static void savePreferences(Context context, Preferences preferences) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context)
                .edit();
        Gson gson = new Gson();
        String userData = gson.toJson(preferences);
        Log.d(TAG, "Saving preferences: [" + userData + "]");
        editor.putString(USER_DATA, userData);
        editor.apply();
    }

    public static Preferences loadPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String userData = prefs.getString(USER_DATA, null);
        Log.d(TAG, "Loading preferences: [" + userData + "]");
        Preferences preferences = (new Gson()).fromJson(userData, Preferences.class);
        if (preferences == null) {
            preferences = new Preferences();
        }
        return preferences;
    }
}
