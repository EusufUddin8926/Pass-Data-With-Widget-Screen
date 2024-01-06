package com.example.widgetandroid;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;


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

        Type typeUserInfo = new TypeToken<List<CurrencyDetails>>() {}.getType();
        List<CurrencyDetails> currencyDetailsList = new Gson().fromJson(dataFromActivity, typeUserInfo);

        // Your widget update logic here
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.example_widget);
        views.setCharSequence(R.id.refreshBtn, "setText", currencyDetailsList.get(0).getCountryName());
        /*Bitmap bitmap = getBitmapFromUri(context, Uri.parse(currencyDetailsList.get(0).getCountryURi()));
        views.setImageViewBitmap(R.id.fromCountryImgView, bitmap);*/

        views.setCharSequence(R.id.fromCountryNameId, "setText", currencyDetailsList.get(0).getCountryName());
        views.setCharSequence(R.id.fromCountryAmountId, "setText", currencyDetailsList.get(0).getAmount());

        // Update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


        Glide.with(context)
                .asBitmap()
                .load(currencyDetailsList.get(0).getCountryURi())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        views.setImageViewBitmap(R.id.fromCountryImgView, resource);

                        // Now update the widget with the modified RemoteViews
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        ComponentName componentName = new ComponentName(context, ExampleAppWidgetProvider.class);
                        appWidgetManager.updateAppWidget(componentName, views);
                    }
                });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction() != null && intent.getAction().equals(ACTION_UPDATE_WIDGET)) {

            // Handle widget update action
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

            if (appWidgetIds != null) {
               // Log.d("TAGonReceive", "onReceive: Called"+ Arrays.toString(appWidgetIds));
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                for (int appWidgetId : appWidgetIds) {
                    updateAppWidget(context, appWidgetManager, appWidgetId);
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            // Load the bitmap from the URI
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) {
                inputStream.close();
            }
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}