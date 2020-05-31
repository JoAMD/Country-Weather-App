package com.unity.mynativeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class CountryWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_weather);

        TextView textView = findViewById(R.id.countryName);
        String res = getIntent().getStringExtra("countryName");
        Log.d("nm", "country name in new activity = " + res);
        textView.setText(res);
    }
}