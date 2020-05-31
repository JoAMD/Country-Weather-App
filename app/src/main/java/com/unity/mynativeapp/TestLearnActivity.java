package com.unity.mynativeapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

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
            HttpURLConnection conn = new HttpsURLConnection(url) {
                @Override
                public String getCipherSuite() {
                    return null;
                }

                @Override
                public Certificate[] getLocalCertificates() {
                    return new Certificate[0];
                }

                @Override
                public Certificate[] getServerCertificates() throws SSLPeerUnverifiedException {
                    return new Certificate[0];
                }

                @Override
                public void disconnect() {

                }

                @Override
                public boolean usingProxy() {
                    return false;
                }

                @Override
                public void connect() throws IOException {

                }
            };

            conn.setDoOutput(true);

            String response = conn.getResponseMessage();

            JSONArray countries = new JSONArray(response).getJSONArray(0);

            String countryName = countries.getJSONObject(0).getString("name");

            TextView textView = findViewById(R.id.city_name);
            textView.setText(countryName);

            CallNextActivity(countryName);

        }
        catch (MalformedURLException e){
            textView2.setText(textView2.getText() + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            textView2.setText(textView2.getText() + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            textView2.setText(textView2.getText() + e.getMessage());
            e.printStackTrace();
        }
    }

    private void CallNextActivity(String countryName){
        Intent intent = new Intent(getApplicationContext(), CountryWeatherActivity.class);
        intent.putExtra("country name", countryName);
        startActivity(intent);
    }

}