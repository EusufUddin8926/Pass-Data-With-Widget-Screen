package com.example.widgetandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";

    private EditText editTextButton;
    private Button confirmBtn;
    private final int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    List<CurrencyDetails> currencyDetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextButton = findViewById(R.id.edit_text_button);
        confirmBtn = findViewById(R.id.confirmBtn);


        currencyDetailsList.add(new CurrencyDetails("Russia", "https://e7.pngegg.com/pngimages/591/293/png-clipart-flag-of-russia-russia-blue-flag-thumbnail.png", "300"));
        currencyDetailsList.add(new CurrencyDetails("Bangladesh", "https://p1.hiclipart.com/preview/902/904/1022/flag-icons-asia-bangladesh-png-icon.jpg", "200"));
        currencyDetailsList.add(new CurrencyDetails("America", "https://image.similarpng.com/very-thumbnail/2020/06/Circle-glossy-american-flag-vector-transparent-PNG.png", "200"));
        currencyDetailsList.add(new CurrencyDetails("India", "https://e7.pngegg.com/pngimages/591/293/png-clipart-flag-of-russia-russia-blue-flag-thumbnail.png", "300"));

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmButtonClicked();
            }
        });


    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        // Send a broadcast to update the widget when the activity is destroyed
        updateWidget(MainActivity.this);

    }*/

    int count  = 0;
    private void confirmButtonClicked() {
        // Save button text to SharedPreferences
        String buttonText = editTextButton.getText().toString();
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("Data", new Gson().toJson(currencyDetailsList));
        editor.putString("string", buttonText);
        editor.apply();

        // Update the widget using the AppWidgetManager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName componentName = new ComponentName(this, ExampleAppWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);

        Intent intent = new Intent(this, ExampleAppWidgetProvider.class);
        intent.setAction(ExampleAppWidgetProvider.ACTION_UPDATE_WIDGET);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(intent);

        finish();
    }
}