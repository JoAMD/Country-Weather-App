package com.unity.mynativeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CountryWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_weather);

        TextView textView = findViewById(R.id.countryName);
        textView.setText(getIntent().getStringExtra("countryName"));
    }
}