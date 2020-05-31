package com.unity.mynativeapp;

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

//            String resultStr = new RetrieveJSONData().execute().get();

            /////////////RetrieveJSONData data = new RetrieveJSONData();
            /////////////          data.SetActivity(this);
            /////////// String resultStr = data.doInBackground(url);
//            String resultStr = new RetrieveJSONData().execute(url);

            new RetrieveJSONData().execute(url);


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void CallNextActivity(String countryName){
        Intent intent = new Intent(getApplicationContext(), CountryWeatherActivity.class);
        intent.putExtra("countryName", countryName);
        startActivity(intent);
    }

    private class RetrieveJSONData extends AsyncTask<URL, Void, String> {

        HttpsURLConnection conn;
        //    ProgressBar progressBar;
//    ProgressDialog dialog;

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

            JSONObject countries = null;
            String countryName = null;
            try {
                countries = new JSONArray(str).getJSONObject(0);
                countryName = countries.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            TextView textView = findViewById(R.id.city_name);
            textView.setText(countryName);

            CallNextActivity(countryName);
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

//class RetrieveJSONData extends AsyncTask<URL, Void, String> {
//
//    HttpsURLConnection conn;
////    ProgressBar progressBar;
////    ProgressDialog dialog;
//    TestLearnActivity activity;
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
////        progressBar.show
////        dialog = ProgressDialog.show()
//        Log.d("nm", "Started fetching!");
//    }
//
//    @Override
//    protected String doInBackground(URL... urls) {
//
//        try{
//
//            int count = urls.length;
//            for (int i = 0; i < count; i++) {
//                conn = (HttpsURLConnection) urls[i].openConnection();
//
//                conn.setConnectTimeout(5);
//                conn.setRequestMethod("GET");
//
//                Object obj = conn.getContent();
//                InputStream inputStream = (InputStream) obj;
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String line;
//                StringBuilder result = new StringBuilder();
//                while ((line = bufferedReader.readLine()) != null){
//                    result.append(line);
//                }
//
//
//                return result.toString();
//            }
//
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    public void SetActivity(TestLearnActivity activity){
//        this.activity = activity;
//    }
//
//    @Override
//    protected void onPostExecute(String resultStr) {
//        super.onPostExecute(resultStr);
//        conn.disconnect();
//
//        JSONArray countries = null;
//        String countryName = null;
//        try {
//            countries = new JSONArray(resultStr).getJSONArray(0);
//            countryName = countries.getJSONObject(0).getString("name");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        TextView textView = activity.findViewById(R.id.city_name);
//        textView.setText(countryName);
//        Log.d("nm", "Done fetching!");
//
//        activity.CallNextActivity(countryName);
//    }
//}