package com.unity.mynativeapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

class CustomArrayAdapter extends ArrayAdapter<TestLearnActivity.CountryDetails> {

    private ArrayList<TestLearnActivity.CountryDetails> objects;
    private int layoutResource;
    private Context context;
    int count = 0;

    public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TestLearnActivity.CountryDetails> objects) {
        super(context, resource, objects);
        this.context = context;
        this.layoutResource = resource;
        this.objects = new ArrayList<>();
        this.objects.addAll(objects);
    }

    @Nullable
    @Override
    public TestLearnActivity.CountryDetails getItem(int position) {
        return super.getItem(position);
    }
//        public CustomArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects) {
//            super(context, resource, textViewResourceId, objects);
//            this.context = context;
//            this.layoutResource = resource;
//            this.textViewResourceId = textViewResourceId;
//            this.objects = new ArrayList<>();
//            this.objects.addAll(objects);
//        }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Log.d("nm", "position = " + position);
        Log.d("nm", "objects.get(position).getName() = " + objects.get(position).getName());
        if(convertView == null){
            count++;
            Log.d("nm", "inflating again " + count);
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResource, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.city_name);
        textView.setText(objects.get(position).getName());

        ImageView imageView = convertView.findViewById(R.id.image1);
        Drawable img;
//        if(objects.get(position).getDrawable() == null){
//        }
//        else{
            img = objects.get(position).getDrawable();
//        }
        imageView.setImageDrawable(img);

//            imageView.setColorFilter((int)new Color().red());
        //img view set too

        return convertView;

    }
}

