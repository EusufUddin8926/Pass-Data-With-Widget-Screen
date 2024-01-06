package com.example.widgetandroid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_UPDATE_WIDGET = "com.example.widgetandroid.UPDATE_WIDGET";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Handle widget update logic
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Retrieve data from SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dataFromActivity = preferences.getString("Data", "");

        // Your widget update logic here
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        views.setCharSequence(R.id.refreshBtn, "setText", dataFromActivity);

        // Update the widget
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        if (intent.getAction() != null && intent.getAction().equals(ACTION_UPDATE_WIDGET)) {

            // Handle widget update action
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            if (appWidgetIds != null) {
                Log.d("TAGonReceive", "onReceive: Called"+ Arrays.toString(appWidgetIds));
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
    }
}