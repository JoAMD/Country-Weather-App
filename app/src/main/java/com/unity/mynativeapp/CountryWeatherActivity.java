package com.unity.mynativeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class CountryWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_weather);

        TextView textView = findViewById(R.id.countryName);
        String intentCountryName = getIntent().getStringExtra("countryName");
        Log.d("nm", "country name in new activity = " + intentCountryName);


        try {

            URL url = new URL(R.string.weather_api_call_part1 + intentCountryName + R.string.weather_api_call_part2);
            new TestLearnActivity.RetrieveJSONData().execute(url); //how to handle onPostExecute call... use top level class means still same..
            // somehow send class details to static class?

        } catch (Exception e){
            e.printStackTrace();
        }

        textView.setText(intentCountryName);
    }
}