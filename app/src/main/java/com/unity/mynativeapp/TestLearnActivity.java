package com.unity.mynativeapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class TestLearnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_learn);

        //CountriesNameApiCall();

    }

    public void CountriesNameApiCall(View v){

        //For Debug
        TextView textView2 = findViewById(R.id.DebugTextView);

        URL url;
        try{
            url = new URL(getResources().getString(R.string.rest_api_countries_name));

            new RetrieveJSONData().execute(url);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Callback(String countryName){
        //CallNext Activity
        Intent intent = new Intent(getApplicationContext(), CountryWeatherActivity.class);
        intent.putExtra("countryName", countryName);
        startActivity(intent);
    }

    // try making it into static as well as top level class for practice
    // use previous SetActivity fn and use null checks
    public static class RetrieveJSONData extends AsyncTask<URL, Void, String> {

        Activity countryInfoActivity;

        HttpsURLConnection conn;
        //    ProgressBar progressBar;
//    ProgressDialog dialog;


        public void SetActivity(Activity activity){
            countryInfoActivity = activity;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//        progressBar.show
//        dialog = ProgressDialog.show()
            Log.d("nm", "Started fetching!");
        }

        @Override
        protected String doInBackground(URL... urls) {

            try{

                Log.d("nm", "background task");
                int count = urls.length;
                for (int i = 0; i < count; i++) {
                    conn = (HttpsURLConnection) urls[i].openConnection();

                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");

                    Object obj = conn.getContent();
                    InputStream inputStream = (InputStream) obj;
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null){
                        result.append(line);
                    }


                    return result.toString();
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            Log.d("nm", "Done fetching!");
            conn.disconnect();
            JSONObject countries = null;
            String countryName = null;
            try {
                countries = new JSONArray(str).getJSONObject(0);
                countryName = countries.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            TextView textView = countryInfoActivity.findViewById(R.id.city_name);
            textView.setText(countryName);

            ((countryInfoActivity.getCallingActivity().getClass().cast(countryInfoActivity))).Callback(countryName);
        }

//        @Override
//        protected void onPostExecute() {
//            super.onPostExecute();
//            Log.d("nm", "Done fetching!");
//
////            CallNextActivity(countryName);
//        }
    }

}
