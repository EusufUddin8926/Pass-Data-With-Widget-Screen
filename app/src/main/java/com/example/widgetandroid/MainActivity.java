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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "prefs";

    private EditText editTextButton;
    private Button confirmBtn;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextButton = findViewById(R.id.edit_text_button);
        confirmBtn = findViewById(R.id.confirmBtn);



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

    private void confirmButtonClicked() {
        // Save button text to SharedPreferences
        String buttonText = editTextButton.getText().toString();
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.putString("Data", buttonText);
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