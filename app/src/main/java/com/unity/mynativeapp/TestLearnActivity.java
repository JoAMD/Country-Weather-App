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
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class TestLearnActivity extends AppCompatActivity{

    private String countryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_learn);

        CountriesNameApiCall();

    }

    public void CountriesNameApiCall(){

        //For Debug
        TextView textView2 = findViewById(R.id.DebugTextView);

        URL url;
        try{
            url = new URL(getResources().getString(R.string.rest_api_countries_name));

            String resultString = new RetrieveJSONData(true).execute(url).get();

            JSONObject countries;
            try {
                countries = new JSONArray(resultString).getJSONObject(0);
                countryName = countries.getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("nm", "country name in act 1 = " + countryName);

            TextView textView = findViewById(R.id.city_name);
            textView.setText(countryName);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void CallNextActivity(View v){


        //CallNext Activity
        Intent intent = new Intent(getApplicationContext(), CountryWeatherActivity.class);
        intent.putExtra("countryName", countryName);
        startActivity(intent);
    }




    // try making it into static as well as top level class for practice
    // use previous SetActivity fn and use null checks
    public static class RetrieveJSONData extends AsyncTask<URL, Void, String>{

        HttpsURLConnection connHttps;
        HttpURLConnection connHttp;
        boolean isHttps = true;
        //    ProgressBar progressBar;
//    ProgressDialog dialog;


        public RetrieveJSONData(boolean isHttps) {
            this.isHttps = isHttps;
            Log.d("nm", "isHttps = " + isHttps);
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

                    Object obj;

                    if(isHttps){
                        connHttps = (HttpsURLConnection) urls[i].openConnection();
//                        connHttps.setConnectTimeout(1000);
                        connHttps.setRequestMethod("GET");
                        obj = connHttps.getContent();
                    }
                    else{
                        connHttp = (HttpURLConnection) urls[i].openConnection();
//                        connHttp.setConnectTimeout(1000);
                        connHttp.setRequestMethod("GET");
                        obj = connHttp.getContent();
                    }



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
            Log.d("nm", "Done fetching! => " + str);
            if(isHttps){
                connHttps.disconnect();
            }
            else{
                connHttp.disconnect();
            }


//            JSONObject countries = null;
//            String countryName = null;
//            try {
//                countries = new JSONArray(str).getJSONObject(0);
//                countryName = countries.getString("name");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }



//            ((countryInfoActivity.getCallingActivity().getClass().cast(countryInfoActivity))).Callback(countryName);

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
