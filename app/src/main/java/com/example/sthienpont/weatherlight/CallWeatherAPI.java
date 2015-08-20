
package com.example.sthienpont.weatherlight;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sthienpont.weatherlight.dataobjects.OpenWeather;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by sthienpont on 20-Aug-15.
 */
public class CallWeatherAPI extends AsyncTask<String, String, String> {
    private static final String TAG = "CallWeatherAPI";

    public OpenWeather openWeather;
    private TextView view;
    private Context context;

    public CallWeatherAPI(TextView view, Context context) {
        this.view = view;
        this.context = context;
        openWeather = null;
    }

    @Override
    protected String doInBackground(String... strings) {
        openWeather = null;
        String url = strings[0];
        InputStream in = null;
        Log.d(TAG, "URL = " + url);

        try {
            URL call = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) call.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            return "";
        }

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(in);

        openWeather = gson.fromJson(reader, OpenWeather.class);
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            DecimalFormat oneDigit = new DecimalFormat("#.0");
            view.setText(
                    oneDigit.format(Double.parseDouble(openWeather.main.temp) - 273.15) + " °C");
        } catch (NullPointerException e) {
            view.setText("N.A.");
            Toast.makeText(context, context.getString(R.string.search_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
