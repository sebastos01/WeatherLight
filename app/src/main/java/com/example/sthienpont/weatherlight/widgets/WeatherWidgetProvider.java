package com.example.sthienpont.weatherlight.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.sthienpont.weatherlight.MainActivity;
import com.example.sthienpont.weatherlight.R;

public class WeatherWidgetProvider extends AppWidgetProvider {

    public static final String DEBUG_TAG = "WeatherWidgetProvider";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        try {
            updateWidgetContent(context, appWidgetManager);
        } catch (Exception e) {
            Log.e(DEBUG_TAG, "Failed", e);
        }
    }

    public static void updateWidgetContent(Context context,
                                           AppWidgetManager appWidgetManager) {



        RemoteViews remoteView = new RemoteViews(context.getPackageName(),
                R.layout.widget_main);

        Intent launchAppIntent = new Intent(context, MainActivity.class);
        PendingIntent launchAppPendingIntent = PendingIntent.getActivity(context,
                0, launchAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.main, launchAppPendingIntent);

        ComponentName tutListWidget = new ComponentName(context,
                WeatherWidgetProvider.class);
        appWidgetManager.updateAppWidget(tutListWidget, remoteView);
    }
}