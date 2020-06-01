package com.unity.mynativeapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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
//        TextView textView2 = findViewById(R.id.DebugTextView);

        URL url;
        try{
            url = new URL(getResources().getString(R.string.rest_api_countries_name));

            String resultString = new RetrieveJSONData(true).execute(url).get();

            JSONObject countries;

            ArrayList<String> countriesList = new ArrayList<>();

//            for (int i = 0; i < 50; ++i){
//                countriesArr[i] = "India" + i;
//            }

            try {
                JSONArray countriesJSONArr = new JSONArray(resultString);
                int increment = (countriesJSONArr.length() - 1) / 50;
                for(int i = 0; i < countriesJSONArr.length(); i++){
//                    if(i*10>countriesJSONArr.length()) i=i/10+1;
                    countries = countriesJSONArr.getJSONObject(i);
                    countryName = countries.getString("name");
                    countriesList.add(countryName);
                    Log.d("nm", "country name in act 1 = " + countryName);
                }
//                if(j != 50){
//                    countries = countriesJSONArr.getJSONObject(countriesJSONArr.length() - 1);
//                    countryName = countries.getString("name");
//                    countriesList[49] = countryName;
//                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ListPopulate listPopulate = new ListPopulate(countriesList);
            listPopulate.SetListView((ListView) findViewById(R.id.countriesList));
            listPopulate.ListPopulateHelper();
            listPopulate.ListOnClick();

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

    public void CallNextActivity(String countryName){

        //Replace spaces with %20
        String countryNameNew = null;
        if(countryName.contains(" ")){
            String[] strSlices = countryName.split(" ");
            StringBuilder countryNameBuilder = new StringBuilder(strSlices[0]);
            for (int i = 1; i < strSlices.length; ++i){
                countryNameBuilder.append("%20").append(strSlices[i]);
            }
            countryNameNew = countryNameBuilder.toString();
        }


        //CallNext Activity
        Intent intent = new Intent(getApplicationContext(), CountryWeatherActivity.class);
        intent.putExtra("countryName", countryName);
        intent.putExtra("countryNameNew", countryNameNew);
        startActivity(intent);
    }


    private class ListPopulate{
        ListView listView;
        private String[] countriesArr;
        ArrayList<String> list;

        public ListPopulate(String[] countriesArr) {
            this.countriesArr = countriesArr;
        }

        public ListPopulate(ArrayList<String> list) {
            this.list = list;
        }

        private void SetListView(ListView listView){
            this.listView = listView;
        }

        private void ListPopulateHelper(){
            ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_view_item, list);
            listView.setAdapter(adapter);
        }

        private void ListOnClick(){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = listView.getItemAtPosition(position).toString();
                    CallNextActivity(str);
                    Log.d("nm", "calling item click => " + str);
                }
            });
        }
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
