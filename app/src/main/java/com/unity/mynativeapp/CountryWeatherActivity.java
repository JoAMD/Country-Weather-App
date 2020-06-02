package com.unity.mynativeapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class CountryWeatherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_weather);

        TextView textView = findViewById(R.id.countryName);
        String intentCountryName = getIntent().getStringExtra("countryName");
        String intentCountryNameNew = getIntent().getStringExtra("countryNameNew");
        Log.d("nm", "country name in new activity = " + intentCountryName);
        Log.d("nm", "country name New in new activity = " + intentCountryNameNew);


        try {

            String urlString = String.format("%s%s%s%s", getString(R.string.weather_api_call_part1), intentCountryNameNew
                                                       , getString(R.string.weather_api_call_part2), BuildConfig.OPENWEATHER_KEY);

            Log.d("nm", "urlString = " + urlString);
            URL url = new URL(urlString);

            String resultString = new RetrieveJSONData(false).execute(url).get(); //how to handle onPostExecute call... use top level class means still same..
            // somehow send class details to static class?

            String weatherRes = null;
            String weatherDesc = null;
            try {
                JSONArray arr = new JSONObject(resultString).getJSONArray("weather");
                Log.d("nm", arr.toString());
                JSONObject weather = arr.getJSONObject(0);
                Log.d("nm", weather.toString());
                weatherRes = weather.getString("main");
                weatherDesc = weather.getString("description");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("nm", weatherRes);
            Log.d("nm", weatherDesc);
//            textView.setLines(3);
            textView.setText(String.format("%s\n\n%s\n%s", intentCountryName, weatherRes, weatherDesc));

            Log.d("nm", textView.getText().toString());

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}