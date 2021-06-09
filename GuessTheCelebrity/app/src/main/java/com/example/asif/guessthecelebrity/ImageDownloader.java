package com.example.asif.guessthecelebrity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.BitSet;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    @Override
    protected Bitmap doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection;

        try{

            url = new URL(strings[0]);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            Bitmap bitmapFactory = BitmapFactory.decodeStream(in);

            return bitmapFactory;


        }catch (Exception e){

            e.printStackTrace();

        }


        return null;
    }
}
