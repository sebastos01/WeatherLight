
package com.example.sthienpont.weatherlight;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.sthienpont.weatherlight.dataobjects.OpenWeather;
import com.example.sthienpont.weatherlight.utils.PreferenceHelper;
import com.example.sthienpont.weatherlight.views.WeatherBox;
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

    private static final String NO_CITY = "nocity";
    private static final String CONNECTION_ERROR = "connectionerror";

    private OpenWeather openWeather;
    private WeatherBox weatherBox;
    private Context context;
    private boolean isCalledFromMainActivity;

    public CallWeatherAPI(WeatherBox weatherBox, Context context,
            boolean isCalledFromMainActivity) {
        this.weatherBox = weatherBox;
        this.context = context;
        openWeather = null;
        this.isCalledFromMainActivity = isCalledFromMainActivity;
    }

    @Override
    protected String doInBackground(String... strings) {
        openWeather = null;
        String city = PreferenceHelper.loadPreferences(context).currentCity;
        if (city.isEmpty()) {
            return NO_CITY;
        }

        String url = context.getString(R.string.api_url) + city;
        InputStream in;

        try {
            Log.d(TAG, "Calling: " + url);
            URL call = new URL(url);
            HttpURLConnection urlConnection = (HttpURLConnection) call.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            return CONNECTION_ERROR;
        }

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(in);

        openWeather = gson.fromJson(reader, OpenWeather.class);
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals(NO_CITY)) {
            return;
        }
        if (result.equals(CONNECTION_ERROR)) {
            showFeedBack(context.getString(R.string.connection_error));
            return;
        }

        try {
            DecimalFormat oneDigit = new DecimalFormat("#.0");
            String text = oneDigit.format(Double.parseDouble(openWeather.main.temp) - 273.15);
            if (weatherBox != null) {
                weatherBox.setCity(context.getString(R.string.city) + " " + openWeather.name + " ("
                        + openWeather.sys.country + ")");
                weatherBox.setTemperature(text + " °C");
                weatherBox.setClouds(openWeather.weather.get(0).description);
            }
            updateAppWidget(text + "°");
        } catch (NullPointerException e) {
            if (weatherBox != null) {
                weatherBox.setNoWeatherInfo();
            }
            showFeedBack(context.getString(R.string.add_error));
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

    private void showFeedBack(String text) {
        if (isCalledFromMainActivity) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }
}
