package com.unity.mynativeapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
//            url = new URL(getResources().getString(R.string.rest_api_countries_name));
            url = new URL(getResources().getString(R.string.teleport_urban_areas));

            String resultString = new RetrieveJSONData(true).execute(url).get();

            JSONObject countries;

            ArrayList<CountryDetails> countriesList = new ArrayList<>();
            String imgURL;
//            for (int i = 0; i < 50; ++i){
//                countriesArr[i] = "India" + i;
//            }

            try {
                //for teleport
                JSONObject main = new JSONObject(resultString);

                JSONArray countriesArrTeleport = main.getJSONObject("_links").getJSONArray("ua:item");

                JSONObject obj;

                int countMax = main.getInt("count");
                for(int i = 0; i < countMax; ++i){
                    obj = countriesArrTeleport.getJSONObject(i);
                    countryName = obj.getString("name");
                    Log.d("nm", "country name in act 1 = " + countryName);
                    imgURL = obj.getString("href") + "images";
                    Drawable img = GetImageDrawableFromURL(imgURL);
                    countriesList.add(new CountryDetails(countryName, img));
                }

                //for restcountries
//                JSONArray countriesJSONArr = new JSONArray(resultString);
////                int increment = (countriesJSONArr.length() - 1) / 50;
//                Log.d("nm", "no of countries = " + countriesJSONArr.length());
//                for(int i = 0; i < countriesJSONArr.length(); i++){
////                    if(i*10>countriesJSONArr.length()) i=i/10+1;
//                    countries = countriesJSONArr.getJSONObject(i);
//                    countryName = countries.getString("name");
//                    countriesList.add(countryName);
//                    Log.d("nm", "country name in act 1 = " + countryName);
//                }

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
        String countryNameNew = countryName;
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

    public class CountryDetails{
        private String name;
        private String imgURL;
        private Drawable drawable;

        public String getName() {
            return name;
        }

        public String getImgURL() {
            return imgURL;
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
        }

        public CountryDetails(String name, Drawable drawable) {
            this.name = name;
            this.drawable = drawable;
        }

        public CountryDetails(String name, String imgURL) {
            this.name = name;
            this.imgURL = imgURL;
        }
    }

    //    private class ImageDownloadThread implements Runnable {
//
//        @Override
//        public void run() {
//
//        }
//    }


    private class CustomListAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    private class ListPopulate{
        ListView listView;
        private String[] countriesArr;
        ArrayList<CountryDetails> list;

        public ListPopulate(String[] countriesArr) {
            this.countriesArr = countriesArr;
        }

        public ListPopulate(ArrayList<CountryDetails> list) {
            this.list = list;
        }

        private void SetListView(ListView listView){
            this.listView = listView;
        }

        private void ListPopulateHelper(){
//            ArrayAdapterHelper();
            CustomListAdapterHelper();
        }

        private void CustomListAdapterHelper(){

            CustomArrayAdapter adapter = new CustomArrayAdapter(getApplicationContext(), R.layout.content_list_view_item, list);
            listView.setAdapter(adapter);

        }

        private void ArrayAdapterHelper(){
//            ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_view_item, list);
//            listView.setAdapter(adapter);
        }

        private void ListOnClick(){
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String str = list.get(position).name;//listView.getItemAtPosition(position).toString();
                    CallNextActivity(str);
                    Log.d("nm", "calling item click => " + str);
                }
            });
        }
    }

    private Drawable GetImageDrawableFromURL(String url){
        try{

            String resultImgStr = new RetrieveJSONData(true).execute(new URL(url)).get();
            String urlImg = new JSONObject(resultImgStr).getJSONArray("photos").getJSONObject(0).getJSONObject("image").getString("mobile");

            Log.d("nm", "urlImg = " + urlImg);

            ImageDownloadThread imgThread = new ImageDownloadThread(urlImg);
            imgThread.join();

            Drawable d = Drawable.createFromStream(imgThread.GetInputStream(), "src name");
            return d;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return  null;
    }

    private class ImageDownloadThread extends Thread {
        String urlImgStr;
        InputStream is;

        public InputStream GetInputStream(){
            return is;
        }

        public ImageDownloadThread(String urlImgStr) {
            this.urlImgStr = urlImgStr;
        }

        @Override
        public void run() {
            try {
                is = (InputStream) new URL(urlImgStr).getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
