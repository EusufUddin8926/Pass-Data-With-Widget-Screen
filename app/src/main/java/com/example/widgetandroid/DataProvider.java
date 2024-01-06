package com.example.widgetandroid;


import static android.R.id.text1;
import static android.R.layout.simple_list_item_1;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class DataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<CurrencyDetails> myListView = new ArrayList<>();
    Context mContext = null;
    private List<RemoteViews> widgetRemoteViewsList = new ArrayList<>();
    public DataProvider(Context context, Intent intent) {
        mContext = context;
        initData();
    }

    @Override
    public void onCreate() {
        initData();
    }


    @Override
    public void onDataSetChanged() {
        initData();
        updateWidgetRemoteViews();
    }

    private void updateWidgetRemoteViews() {
        widgetRemoteViewsList.clear();

        for (int i = 0; i < myListView.size(); i++) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.single_list_item);
            remoteViews.setTextViewText(R.id.countryNmeTxt, myListView.get(i).getCountryName());
            remoteViews.setTextViewText(R.id.currencyTxtId, myListView.get(i).getAmount());

            // Load image asynchronously
            Bitmap imageBitmap = loadImage(myListView.get(i).getCountryURi());
            remoteViews.setImageViewBitmap(R.id.fromCountryImgView, imageBitmap);

            widgetRemoteViewsList.add(remoteViews);
        }
    }

    // Load image using Glide and return the Bitmap synchronously
    private Bitmap loadImage(String imageUrl) {
        try {
            return Glide.with(mContext)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroy() {



    }


    @Override
    public int getCount() {
        return myListView.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
      /*  RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.single_list_item);
        view.setTextViewText(R.id.countryNmeTxt, myListView.get(position).getCountryName());
        view.setTextViewText(R.id.currencyTxtId, myListView.get(position).getAmount());
        Glide.with(mContext)
                .asBitmap()
                .load(myListView.get(position).getCountryURi())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        view.setImageViewBitmap(R.id.fromCountryImgView, resource);

                        // Now update the widget with the modified RemoteViews
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(mContext);
                        ComponentName componentName = new ComponentName(mContext, ExampleAppWidgetProvider.class);
                        appWidgetManager.updateAppWidget(componentName, view);
                    }
                });
        return view;*/
        return widgetRemoteViewsList.get(position);
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        myListView.clear();
        // Retrieve data from SharedPreferences
        SharedPreferences preferences = mContext.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String dataFromActivity = preferences.getString("Data", "");

        Type typeUserInfo = new TypeToken<List<CurrencyDetails>>() {}.getType();
        List<CurrencyDetails> currencyDetailsList = new Gson().fromJson(dataFromActivity, typeUserInfo);

        myListView.addAll(currencyDetailsList);
    }
    }



