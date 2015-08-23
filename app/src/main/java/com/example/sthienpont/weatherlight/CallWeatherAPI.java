
package com.example.sthienpont.weatherlight;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sthienpont.weatherlight.dataobjects.OpenWeather;
import com.example.sthienpont.weatherlight.widgets.WeatherWidgetProvider;
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
    private String city;
    private Context context;

    public CallWeatherAPI(TextView view, Context context) {
        this.view = view;
        this.context = context;
        openWeather = null;
    }

    @Override
    protected String doInBackground(String... strings) {
        openWeather = null;
        String url = strings[0] + strings[1];
        city = strings[1];
        InputStream in = null;

        try {
            Log.d(TAG, "Calling: " + url);
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
            String text = oneDigit.format(Double.parseDouble(openWeather.main.temp) - 273.15);
            view.setText(text + " °C");
            updateAppWidget(text + "°");
        } catch (NullPointerException e) {
            view.setText("N.A.");
            Toast.makeText(context, context.getString(R.string.add_error), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void updateAppWidget(String temperature) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_main);

        ComponentName thisWidget = new ComponentName(context, WeatherWidgetProvider.class);
        remoteViews.setTextViewText(R.id.tvWidgetTemperature, temperature);
        remoteViews.setTextViewText(R.id.tvWidgetCityName, openWeather.name);
        setIconWidgetIcon(remoteViews, openWeather.weather.get(0).icon);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private void setIconWidgetIcon(RemoteViews remoteViews, String icon) {
        if (icon.contains("01")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_01);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("02")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_02);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("03")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_03);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("04")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_04);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("09")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_09);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("10")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_10);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("11")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_11);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("13")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_13);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
        if (icon.contains("50")) {
            remoteViews.setImageViewResource(R.id.ivWidgetIcon, R.drawable.ic_50);
            remoteViews.setViewVisibility(R.id.ivWidgetIcon, View.VISIBLE);
        }
    }
}
